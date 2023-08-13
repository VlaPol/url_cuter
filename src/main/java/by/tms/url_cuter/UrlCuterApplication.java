package by.tms.url_cuter;

import by.tms.url_cuter.configure.BlackListConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BlackListConfig.class)
public class UrlCuterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlCuterApplication.class, args);
    }

}
