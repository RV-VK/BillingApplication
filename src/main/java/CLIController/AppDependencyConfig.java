package CLIController;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "CLIController,Service,Entity,DAO")
public class AppDependencyConfig {

}
