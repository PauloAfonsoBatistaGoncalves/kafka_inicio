package br.com.alura.ecommerce;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class FraudDetectorService {
	public static void main(String[] args)  {
		var fraudService = new FraudDetectorService();
		try(var service = new KafkaService<Order>(
				FraudDetectorService.class.getSimpleName(), 
				"ECOMMERCE_NEW_ORDER", fraudService::parse, 
				Order.class,
				Map.of())){
			service.run();
		}
	}

	private void parse(ConsumerRecord<String, Order> record) {
		System.out.println("-----------------------------------------------------------------");
		System.out.println("Processing new order, cheking for fraud");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Order processed");
	}
}
