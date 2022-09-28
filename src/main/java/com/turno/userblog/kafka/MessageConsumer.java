package com.turno.userblog.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.Gson;
import com.turno.config.ApplicationContextProvider;
import com.turno.dao.UserDao;
import com.turno.userblog.model.User;

public class MessageConsumer implements Runnable{

	KafkaConsumer<String, String> consumer;
	
	public MessageConsumer(String topic){
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		props.setProperty("enable.auto.commit", "false");
		props.setProperty("group.id", "turno");

		consumer = new KafkaConsumer<String, String>(props);
	}
	
	

	@Override
	public void run() {
		consumer.subscribe(Collections.singletonList("user"));
		UserDao userDao = ApplicationContextProvider.getBean(UserDao.class);	//manual dependency injection for specified class
		while (true) {

			ConsumerRecords<String, String> orders = consumer.poll(Duration.ofSeconds(1));

			for (ConsumerRecord<String, String> order : orders) {
				System.out.println("topic = " + order.key());
				System.out.println("message = " + order.value());
				User user = new Gson().fromJson(order.value(), User.class);
				userDao.saveOrUpdate(user);
			}

			consumer.commitAsync();
		}
	}
}
