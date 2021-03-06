package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrder {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		try(var orderDispatcher = new KafkaDispatcher<Order>()){
			try(var emailDispatcher = new KafkaDispatcher<Email>()){
				for (int i = 0; i < 10; i++) {
					var orderId = UUID.randomUUID().toString();
					var amount = new BigDecimal(Math.random() * 5000 + 1);
					var email = Math.random() + "@email.com";
					
					var order = new Order(orderId, amount, email);
					orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

					
					
					var emailCode = new Email("teste@teste.com", "Thank you for your order! We arre processing yout order!");
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);
				}
			}
		}
	}


}
