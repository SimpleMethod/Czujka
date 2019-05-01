package pl.simplemethod.czujka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import pl.simplemethod.czujka.botparser.BotController;
import pl.simplemethod.czujka.botparser.StringParser;
import pl.simplemethod.czujka.repository.UsersRepository;
import pl.simplemethod.czujka.repository.UsersRepositoryImpl;

@SpringBootApplication
@EntityScan("pl.simplemethod.czujka")
@ComponentScan("pl.simplemethod.czujka")
public class CzujkaApplication {


    @Bean
    public UsersRepository usersRepository() {
        return new UsersRepositoryImpl();
    }

	@Bean
	public BotController botController() {
		return  new BotController("xoxb-609016061122-622647790118-rXhxGVP2E1tyPPi6Redpx8O4", "iA2NXCB2ykk6Rhk6kkGHlB6o", "CJBC4QZLY");
	}

	@Bean
	public StringParser stringParser()
	{
		return new StringParser();
	}

	@Bean
	public String string()
	{
		return "";
	}

	public static void main(String[] args) {
		SpringApplication.run(CzujkaApplication.class, args);

	}

}
