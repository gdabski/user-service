package gdabski.demo.user.conf;

import static org.springframework.http.HttpMethod.*;

import gdabski.demo.user.repository.UserRepository;
import gdabski.demo.user.security.DefaultUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Contains Spring configuration for web security.
 */
@Configuration
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new DefaultUserDetailsService(userRepository);
    }

    @Configuration
    @Order(100)
    static class GeneralWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

        /* Very dirty. */
        private static final String ADMIN_OR_SELF = "hasRole('ADMIN') or " +
                "!(principal instanceof T(String)) and T(Integer).valueOf(#id) == principal.id";

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                        .antMatchers(DELETE, "/users/{id}")
                            .access(ADMIN_OR_SELF)
                        .antMatchers(PATCH, "/users/{id}")
                            .access(ADMIN_OR_SELF)
                        .antMatchers("/error/**").permitAll()
                        .antMatchers(POST, "/users").anonymous()
                        .anyRequest().authenticated()
                    .and().httpBasic()
                    .and().csrf().disable();
        }

    }

}
