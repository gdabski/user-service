package gdabski.demo.user.it.conf;

import static java.util.Objects.requireNonNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("it")
class DatabaseUrlVerifier {

    @EventListener
    public void onContextRefresh(ContextRefreshedEvent event) {
        String dbUrl = event.getApplicationContext().getEnvironment().getProperty("spring.datasource.url");
        if (!requireNonNull(dbUrl).startsWith("jdbc:h2:mem")) {
            log.error("Detected non-memory database configured for IT tests. " +
                    "Tests will halt to prevent accidental overwriting of the database.");
            throw new IllegalArgumentException("Non-memory database configured as 'spring.datasource.url'.");
        }
    }

}
