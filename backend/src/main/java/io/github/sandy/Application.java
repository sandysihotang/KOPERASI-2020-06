package io.github.sandy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages="io.github.sandy")
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }
}
