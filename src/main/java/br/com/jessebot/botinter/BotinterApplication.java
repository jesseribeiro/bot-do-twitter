package br.com.jessebot.botinter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class BotinterApplication implements CommandLineRunner {


	public static void main(String[] args) throws IOException {
		SpringApplication.run(BotinterApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		postaTweet.executar();
		/*
		RequestMessageDTO dto = new RequestMessageDTO();
		dto.setTo("conceicao.roberto@gmail.com");
		dto.setMessage("Teste Bob, envio de email");
		dto.setSubject("Teste serviço de mensagens - Pratico");
		dto.setTipo("EMAIL");

		messageManagerService.sendMessage(dto);
		*/
	}
}
