package zhaw.galeajua.ml2_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import zhaw.galeajua.ml2_project.config.HintsRegistrar;

@ImportRuntimeHints(HintsRegistrar.class)
@SpringBootApplication
public class Ml2ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ml2ProjectApplication.class, args);
	}

}
