package com.turno.userblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.turno.userblog.kafka.MessageConsumer;

@SpringBootApplication(scanBasePackages = { "com.turno" })
public class UserblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserblogApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void callConsumerAfterStartup() {

		new MessageConsumer("user").run();
//		new MessageConsumer("blog").run();
	}
}
