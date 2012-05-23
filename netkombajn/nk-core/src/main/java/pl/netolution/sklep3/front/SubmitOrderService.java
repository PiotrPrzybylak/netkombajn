package pl.netolution.sklep3.front;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.exception.EmptyOrderException;

public interface SubmitOrderService {

	@Transactional
	void submitOrder(Order order) throws EmptyOrderException;

}