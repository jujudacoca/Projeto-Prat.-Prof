package api.avaliadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class AvaliadinApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvaliadinApplication.class, args);
	}

}
