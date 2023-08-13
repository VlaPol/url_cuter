package by.tms.url_cuter.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
@ConfigurationProperties(prefix = "block")
@Getter
@Setter
public class BlackListConfig {

    List<String> blackList;
}
