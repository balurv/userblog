package com.turno.userblog.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MessageProducer {

	private KafkaProducer<String, String> producer ;
	private static MessageProducer messageProducerInstance;
	private MessageProducer(){
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty("max.request.size", "104857600");
		
		producer = new KafkaProducer<String, String>(props);
		
	}
	public static MessageProducer getInstance() {
		if(messageProducerInstance == null) {
			synchronized (MessageProducer.class) {
				if(messageProducerInstance == null) {
					messageProducerInstance = new MessageProducer();
				}
			}
		}
		return messageProducerInstance;
	}
	public void addMessage(String topic, String message ) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, topic, message);
		try {
			producer.send(record, new MessageCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
