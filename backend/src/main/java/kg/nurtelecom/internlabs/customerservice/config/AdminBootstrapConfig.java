package kg.nurtelecom.internlabs.customerservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdminBootstrapProperties.class)
public class AdminBootstrapConfig {
}
