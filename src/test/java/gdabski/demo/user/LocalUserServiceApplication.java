package gdabski.demo.user;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class LocalUserServiceApplication {

    private LocalUserServiceApplication() {}

    public static void main(String[] args) {
        new SpringApplicationBuilder(UserServiceApplication.class)
                .profiles("dev")
                .run(args);
    }

}
