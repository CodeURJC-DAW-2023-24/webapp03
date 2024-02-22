package es.codeurjc.holamundo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        //USER
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("pass"))
            .roles("USER")
            .build();
        //ADMIN
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("adminpass"))
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authenticationProvider(authenticationProvider());
        
        //Public pages
        http
            .authorizeHttpRequests(authorize -> authorize
                //Public pages
                .requestMatchers("/","/loginPage.html", "assets/**", "css/**", "js/**", "templates/**").permitAll()
                //Private pages
                .requestMatchers("/profile/**").hasAnyRole("USER")
                .requestMatchers("/book/**").hasAnyRole("USER")
                .requestMatchers("/search/**").hasAnyRole("USER")
                .requestMatchers("/errorPage/**").hasAnyRole("USER")
                .requestMatchers("/static/**").hasAnyRole("USER")
                .requestMatchers("/admin").hasAnyRole("ADMIN"))
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/profile/FanBook_785")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll());

        //Disable CSRF 
        http.csrf(csrf -> csrf.disable());

        return http.build();


    }
    
}
