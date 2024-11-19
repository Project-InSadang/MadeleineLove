package sideproject.madeleinelove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class MadeleineLoveApplication {

	public static void main(String[] args) {
		SpringApplication.run(MadeleineLoveApplication.class, args);
	}

}
