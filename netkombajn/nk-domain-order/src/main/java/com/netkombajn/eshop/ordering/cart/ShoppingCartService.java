package com.netkombajn.eshop.ordering.cart;

import java.util.ArrayList;
import java.util.List;

import org.veripacks.Export;

import com.netkombajn.eshop.ordering.order.item.OrderItem;
import com.netkombajn.eshop.ordering.shipment.ShipmentOption;
import com.netkombajn.eshop.ordering.shipment.ShipmentOptionDao;
import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.dao.ProductDao;

@Export
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
