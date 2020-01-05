package gdabski.demo.user.it.conf;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

import java.lang.annotation.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * A <em>composed annotation</em> that enables the Spring Boot test framework, specifies
 * the Spring context including the application and additional testing amenities and
 * enables the <i>it</i> Spring profile.
 *
 * @see ActiveProfiles
 * @see SpringBootTest
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ActiveProfiles("it")
@Sql(scripts = "/sql/truncate-tables.sql", executionPhase = AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = ISOLATED))
@SpringBootTest(webEnvironment = RANDOM_PORT)
public @interface ITTest {
}
