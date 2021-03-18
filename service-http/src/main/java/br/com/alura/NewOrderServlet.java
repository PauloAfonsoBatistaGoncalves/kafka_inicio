package br.com.alura;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import br.com.alura.ecommerce.Email;
import br.com.alura.ecommerce.KafkaDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewOrderServlet extends HttpServlet {

	private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();
	private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<Email>();

	@Override
	public void destroy() {
		super.destroy();
		orderDispatcher.close();
		emailDispatcher.close();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			var orderId = UUID.randomUUID().toString();
			var email = req.getParameter("email");

			var amount = new BigDecimal(req.getParameter("amount"));

			var order = new Order(orderId, amount, email);
			orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);



			var emailCode = new Email("teste@teste.com", "Thank you for your order! We arre processing yout order!");
			emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);

			System.out.println("New order sent successfully!");

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().println("New order sent.");
		} catch (InterruptedException e) {
			throw new ServletException(e);
		} catch (ExecutionException e) {
			throw new ServletException(e);
		}
	}

}
