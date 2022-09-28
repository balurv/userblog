package com.turno.userblog.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class MessageCallback implements Callback{
	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		if(exception != null) {
			exception.printStackTrace();
		}
		System.out.println(metadata.partition());
		System.out.println(metadata.offset());
		System.out.println("message sent successfully!");
		
	}
}
