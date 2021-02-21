package br.com.alura.ecommerce;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class EmailService {
	public static void main(String[] args)  {
		var emailservice = new EmailService();
		try(var service = new KafkaService(
				EmailService.class.getSimpleName() ,
				"ECOMMERCE_SEND_EMAIL", emailservice::parse, Email.class,
				Map.of())){
			service.run();
		}
	}

	private void parse(ConsumerRecord<String, Email> record) {
		System.out.println("-----------------------------------------------------------------");
		System.out.println("Send email");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Email sent");
	}
	



}
