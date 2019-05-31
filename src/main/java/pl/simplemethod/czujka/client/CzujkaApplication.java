package pl.simplemethod.czujka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.simplemethod.czujka.botparser.BotController;
import pl.simplemethod.czujka.botparser.StringParser;

@SpringBootApplication
@EntityScan("pl.simplemethod.czujka")
@ComponentScan("pl.simplemethod.czujka")
@Configuration
@EnableJpaRepositories(basePackages = "pl.simplemethod.czujka.repository")
public class CzujkaApplication {

    @Bean
	public ScheduledService scheduledService() {
		return new ScheduledService();
	}

	@Bean
	public BotController botController() {
		return new BotController("xoxp-609016061122-620444144032-640355043890-0830fb3a325c2884b8bc923b311f3247", "CmWERIAY9Rs5dOqJfLTG90n3", "CJBC4QZLY");
	}

	@Bean
	public StringParser stringParser() {
		return new StringParser();
	}

	@Bean
	public String string() {
		return "";
	}

	public static void main(String[] args) {
		SpringApplication.run(CzujkaApplication.class, args);
	}
}
