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
        //AUTHOR
        UserDetails author = User.builder()
            .username("author")
            .password(passwordEncoder().encode("authorpass"))
            .roles("USER", "AUTHOR")
            .build();
        //ADMIN
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("adminpass"))
            .roles("AUTHOR", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin, author);
    }

    

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authenticationProvider(authenticationProvider());
        
        //Public pages
        http
            .authorizeHttpRequests(authorize -> authorize
                //Public pages
                .requestMatchers("assets/**", "css/**", "js/**", "templates/**",
                    "/book/*", "/book/*/loadMoreReviews", "/errorPage/**", "/", 
                    "/landingPage/loadMore", "/landingPage/mostReadGenres",  
                    "/loginError", "/profile/*", "/profile/*/loadMore", "/search/**", 
                    "loginPage.html").permitAll()
                //Private pages
                //USER
                .requestMatchers("/profile/*/edit", "/book/*/addReview", 
                    "/book/*/deleteReview/*").hasAnyRole("USER")
                //AUTHOR
                .requestMatchers("/book/*/edit", "/modifyDone/*").hasAnyRole("AUTHOR")
                //ADMIN
                .requestMatchers("/admin/**").hasAnyRole("ADMIN"))
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/")
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
