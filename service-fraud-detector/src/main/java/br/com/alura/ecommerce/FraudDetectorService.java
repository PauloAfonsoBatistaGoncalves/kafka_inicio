package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

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
	
	private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

	private void parse(ConsumerRecord<String, Order> record) throws InterruptedException, ExecutionException {
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
		
		var order = record.value();
		if(isFraud(order)) {
			System.out.println("Order is a fraud!!!");
			orderDispatcher.send("ECOMMERCER_ORDER_REJECTED", order.getEmail(), order);
		}else {
			System.out.println("Approved: " + order);
			orderDispatcher.send("ECOMMERCER_ORDER_APPROVED", order.getEmail(), order);

		}
		
		
	}

	private boolean isFraud(Order order) {
		return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
	}
}
