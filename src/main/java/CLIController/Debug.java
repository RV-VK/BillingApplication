package CLIController;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Debug {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppDependencyConfig.class);
		LoginCLI loginCLI = applicationContext.getBean(LoginCLI.class);
	}
}
