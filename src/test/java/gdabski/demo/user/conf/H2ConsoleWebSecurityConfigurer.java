package gdabski.demo.user.conf;

import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Removes security restrictions for viewing the H2 web console.
 * <p>
 * Typically the affected path is {@code /h2-console/**} but can be changed
 * by configuration properties.
 */
@Configuration
@Profile("dev")
@Order(99) // should come before the GeneralWebSecurityConfigurer
public class H2ConsoleWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    public H2ConsoleWebSecurityConfigurer(H2ConsoleProperties properties) {
        path = properties.getPath() + "/**";
    }

    private final String path;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(path).authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
