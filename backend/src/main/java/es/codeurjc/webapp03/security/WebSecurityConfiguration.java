package es.codeurjc.webapp03.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import es.codeurjc.webapp03.security.jwt.JwtRequestFilter;
import es.codeurjc.webapp03.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public RepositoryUserDetailService userDetailService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(unauthorizedHandlerJwt)
                );

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PRIVATE ENDPOINTS
                        // USER
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/*").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/genres/me").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/books/me/recommended").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/authors/me").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/books/*/read").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/books/*/reading").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/books/*/wanted").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/*/lists").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/books/me").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/authors/{username}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{username}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole("ADMIN", "AUTHOR")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("USER")
                        //SPA Controller for frontend
                        .requestMatchers("/new/**").permitAll()


                        // PUBLIC ENDPOINTS (anything that's not filtered by the above rules, is public. It is not necessary to add anything here)
                        .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        //Public pages
        http
                .authorizeHttpRequests(authorize -> authorize
                        //Public pages
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "templates/**",
                                "/book/*", "/book/*/loadMoreReviews", "/errorPage/**", "/",
                                "/landingPage/loadMore", "/landingPage/mostReadGenres",
                                "/loginError", "/profile/*", "/profile/*/loadMore", "/profile/*/exportLists", "/search/**",
                                "/login", "/login", "/login/**", "/signup/**", "/signup", "/error/**", "/mostReadGenres/**", "/v3/**", "/swagger-ui/**").permitAll()
                        //Private pages
                        //USER
                        .requestMatchers("/profile/*/edit", "/profile/*/upload", "/book/*/addReview",
                                "/book/*/deleteReview/*", "/book/*/add/**", "/profile/*/editPassword", "/book/*/removeFromLists/**").hasAnyRole("USER")
                        //AUTHOR
                        .requestMatchers("/book/*/edit", "/modifyDone/*").hasAnyRole("AUTHOR", "ADMIN")
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
