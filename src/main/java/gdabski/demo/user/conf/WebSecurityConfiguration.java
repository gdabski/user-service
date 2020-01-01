package gdabski.demo.user.conf;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

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

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                        .antMatchers(POST, "/user").anonymous()
                        .antMatchers(DELETE, "/user/*").access("hasRole('ADMIN')")
                        // User should also be able to delete own account. This would require extending
                        // the used UserDetails implementation with id field.
                        .antMatchers("/error/**").permitAll()
                        .anyRequest().authenticated()
                    .and().httpBasic()
                    .and().csrf().disable();
        }

    }

}
