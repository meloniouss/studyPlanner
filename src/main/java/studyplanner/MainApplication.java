package studyplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
		System.out.println("test");
	}

	//service -> interacts with DB (data layer)
	//entity -> kind of like an object in the database
	//controller -> handle HTTP requests, interact with services, and return responses.
}
