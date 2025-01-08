package by.giorgi.jobportalback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class JobPortalBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalBackApplication.class, args);
	}

}
