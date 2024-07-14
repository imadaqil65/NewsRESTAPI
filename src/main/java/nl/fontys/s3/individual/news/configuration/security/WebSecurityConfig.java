package nl.fontys.s3.individual.news.configuration.security;

import nl.fontys.s3.individual.news.configuration.security.auth.AuthenticationRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebSecurityConfig {

    private static final String[] SWAGGER_UI_RESOURCES = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**"};
    private static final String JOURNALIST = "JOURNALIST";
    private static final String ADMIN = "ADMIN";
    private static final String NEWS_MAP = "/news";
    private static final String ALL_TOPICS_MAP = "/topics/**";
    private static final String COMMENT_MAP = "/comments";
    private static final String TOPICS_MAP = "/topics";

    // Suppress SonarQube security hotspot warning for disabling CSRF protection
    @SuppressWarnings("squid:S4502")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           AuthenticationRequestFilter authenticationRequestFilter) throws Exception {
                httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // CSRF is disabled because the application uses token-based authentication which is stateless and doesn't require CSRF protection.
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                //POST
                                .requestMatchers(HttpMethod.POST, "/users", "/tokens", "/notifications").permitAll()
                                .requestMatchers(HttpMethod.POST, NEWS_MAP).hasRole(JOURNALIST)
                                .requestMatchers(HttpMethod.POST, TOPICS_MAP, "users/journalist", "users/admin").hasRole(ADMIN)
                                //PUT
                                .requestMatchers(HttpMethod.PUT, "/users/password").permitAll()
                                .requestMatchers(HttpMethod.PUT, NEWS_MAP).hasRole(JOURNALIST)
                                .requestMatchers(HttpMethod.PUT, ALL_TOPICS_MAP).hasRole(ADMIN)
                                //GET
                                .requestMatchers(HttpMethod.GET, COMMENT_MAP, NEWS_MAP, TOPICS_MAP, ALL_TOPICS_MAP, "/news/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "topics/details").hasRole(ADMIN)
                                //DELETE
                                .requestMatchers(HttpMethod.DELETE, NEWS_MAP).hasRole(JOURNALIST)
                                .requestMatchers(HttpMethod.DELETE, ALL_TOPICS_MAP).hasRole(ADMIN)
                                .requestMatchers("/ws/**").permitAll()
                                //SWAGGER
                                .requestMatchers(SWAGGER_UI_RESOURCES).permitAll() // Swagger is also public (In "real life" it would only be public in non-production environments)
                                .anyRequest().authenticated()                      // Everything else --> authentication required, which is Spring security's default behaviour
                )
                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:5173", "http://localhost:5174",
                                "http://localhost:3000", "http://localhost:3090")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
