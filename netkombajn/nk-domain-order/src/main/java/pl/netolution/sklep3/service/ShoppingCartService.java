package pl.netolution.sklep3.service;

import java.util.ArrayList;
import java.util.List;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.ShipmentOptionDao;
import pl.netolution.sklep3.domain.OrderItem;
import pl.netolution.sklep3.domain.ShipmentOption;
import pl.netolution.sklep3.domain.ShoppingCart;

public class ShoppingCartService {

	private ProductDao productDao;
	private ShipmentOptionDao shipmentOptionDao;

	public ShoppingCartService(ProductDao productDao, ShipmentOptionDao shipmentOptionDao) {
		this.productDao = productDao;
		this.shipmentOptionDao = shipmentOptionDao;
	}

	public void refreshProducts(ShoppingCart shoppingCart) {

		for (OrderItem orderItem : shoppingCart.getItems()) {
			productDao.refresh(orderItem.getProduct());
		}
	}

	public List<ShipmentOption> getShipmentOptions(ShoppingCart cart) {
		List<ShipmentOption> shipmentOptions = shipmentOptionDao.getAll();
		List<ShipmentOption> inRangeOptions = getOptionsInPriceRange(cart.getTotalPrice(), shipmentOptions);
		List<ShipmentOption> availableShipmentOptions = getAvailableShipmentOptions(cart, inRangeOptions);

		return availableShipmentOptions;
	}

	private List<ShipmentOption> getAvailableShipmentOptions(ShoppingCart cart, List<ShipmentOption> inRangeOptions) {
		List<ShipmentOption> availableShipmentOptions = new ArrayList<ShipmentOption>();

		if (cart.isAvailableInstantly()) {
			return inRangeOptions;
		}
		for (ShipmentOption shipmentOption : inRangeOptions) {
			if (!shipmentOption.isAllowOnlyInstantProducts()) {
				availableShipmentOptions.add(shipmentOption);
			}
		}

		return availableShipmentOptions;
	}

	private List<ShipmentOption> getOptionsInPriceRange(Price totalShoppingPrice, List<ShipmentOption> shipmentOptions) {
		List<ShipmentOption> inRangeOptions = new ArrayList<ShipmentOption>();

		for (ShipmentOption shipmentOption : shipmentOptions) {
			if (shipmentOption.isInRange(totalShoppingPrice)) {
				inRangeOptions.add(shipmentOption);
			}
		}
		return inRangeOptions;
	}

}
