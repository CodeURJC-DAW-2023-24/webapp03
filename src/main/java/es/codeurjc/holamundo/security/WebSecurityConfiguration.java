package es.codeurjc.holamundo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    public RepositoryUserDetailService userDetailService;

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authenticationProvider(authenticationProvider());

        //Public pages
        http
            .authorizeHttpRequests(authorize -> authorize
                //Public pages
                .requestMatchers("/assets/**", "/css/**", "/js/**", "templates/**",
                    "/book/*", "/book/*/loadMoreReviews", "/errorPage/**", "/",
                    "/landingPage/loadMore", "/landingPage/mostReadGenres",
                    "/loginError", "/profile/*", "/profile/*/loadMore", "/search/**",
                    "/login", "/login", "/login/**", "/signup/**", "/signup", "/error/**", "/mostReadGenres/**").permitAll()
                //Private pages
                //USER
                .requestMatchers("/profile/*/edit", "/profile/*/upload", "/book/*/addReview",
                    "/book/*/deleteReview/*", "/book/*/add/**", "/profile/*/editPassword", "/book/*/removeFromLists/**").hasAnyRole("USER")
                //AUTHOR
                .requestMatchers("/book/*/edit", "/modifyDone/*").hasAnyRole("AUTHOR","ADMIN")
                //ADMIN
                .requestMatchers("/**").hasRole("ADMIN"))
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll());

        return http.build();
    }

}
