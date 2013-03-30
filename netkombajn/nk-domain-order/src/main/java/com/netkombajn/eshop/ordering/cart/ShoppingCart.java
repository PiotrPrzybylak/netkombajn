package com.netkombajn.eshop.ordering.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.StockKeepingUnit;

import com.netkombajn.eshop.ordering.order.item.CompositeOrderItem;
import com.netkombajn.eshop.ordering.order.item.OrderItem;
import com.netkombajn.eshop.ordering.order.item.SkuOrderItem;
import com.netkombajn.store.domain.shared.price.Price;

public class ShoppingCart {

	private static final Logger log = Logger.getLogger(ShoppingCart.class);

	//TODO jak zakup sku się sprawdzi wywalić
	private final Map<Long, OrderItem> items = new HashMap<Long, OrderItem>();

	private final Map<Long, SkuOrderItem> skuItems = new HashMap<Long, SkuOrderItem>();

	private Map<Long, CompositeOrderItem> compositeOrderItems = new HashMap<Long, CompositeOrderItem>();

	//TODO uzywana tylko w testach
	public void addItem(Product product) {
		addItem(product, 1);
	}

	public void addItem(Product product, int quantity) {

		if (quantity < 1) {
			throw new RuntimeException("Quantity must be > 0");
		}

		OrderItem item = items.get(product.getId());

		if (item != null) {
			item.increaseQuantity(quantity);
		} else {
			item = new OrderItem(product, quantity);
			items.put(product.getId(), item);
		}

		item.setUnitPrice(product.getRetailGrossPrice());

		log.debug("Product: " + product + " added to shopping cart. Total products: " + getItemCount());
	}

	public void addSkuItem(StockKeepingUnit sku, int quantity) {

		if (quantity < 1) {
			throw new RuntimeException("Quantity must be > 0");
		}

		SkuOrderItem item = skuItems.get(sku.getId());

		if (item != null) {
			item.increaseQuantity(quantity);
		} else {
			item = new SkuOrderItem(sku, quantity);
			skuItems.put(sku.getId(), item);
		}

		item.setUnitPrice(sku.getFinalPrice());

		log.debug("Sku: " + sku + " added to shopping cart. Total products: " + getItemCount());
	}

	public int getItemCount() {
		int count = 0;
		for (OrderItem item : items.values()) {
			count += item.getQuantity();
		}

		for (SkuOrderItem item : skuItems.values()) {
			count += item.getQuantity();
		}
		return count;
	}

	public Price getTotalPrice() {
		Price total = new Price();
		for (OrderItem item : items.values()) {
			total = total.add(item.getTotalPrice());
		}

		for (CompositeOrderItem compositeOrderItem : compositeOrderItems.values()) {
			//TODO implement getTotalPrice
			total = total.add(compositeOrderItem.getUnitPrice());
		}

		for (SkuOrderItem skuOrderItem : skuItems.values()) {

			total = total.add(skuOrderItem.getTotalPrice());
		}

		return total;
	}

	public boolean isEmpty() {
		return getItemCount() == 0 && compositeOrderItems.size() == 0;
	}

	public void clear() {
		items.clear();
		skuItems.clear();
		compositeOrderItems.clear();
	}

	public void removeItem(long productId) {
		items.remove(productId);
	}

	public void removeSkuItem(long skuId) {
		skuItems.remove(skuId);
	}

	public void removeCompositeItem(long compositeId) {
		compositeOrderItems.remove(compositeId);
	}

	public void setQuantity(long productId, int quantity) {
		if (items.get(productId) != null) {
			if (quantity == 0) {
				removeItem(productId);
			} else {
				items.get(productId).setQuantity(quantity);
			}
		} else {
			log.debug("Product with id: " + productId + " is not present in shopping cart. Ignoring setQuantity call");
		}

	}

	public void setSkuQuantity(long skuId, int quantity) {
		if (skuItems.get(skuId) == null) {
			log.debug("Sku with id: " + skuId + " is not present in shopping cart. Ignoring setQuantity call");
			return;
		}
		if (quantity == 0) {
			removeSkuItem(skuId);
		} else {
			skuItems.get(skuId).setQuantity(quantity);
		}

	}

	public void addCompositeOrderItem(CompositeOrderItem compositeOrderItem) {
		if (compositeOrderItem == null) {
			throw new RuntimeException("composite order item was null");
		}

		compositeOrderItems.put(compositeOrderItem.getCompositeProduct().getId(), compositeOrderItem);

	}

	public boolean isAvailableInstantly() {

		for (SkuOrderItem skuOrderItem : skuItems.values()) {
			StockKeepingUnit sku = skuOrderItem.getSku();
			if (!sku.getAvailability().isInstantAvailable()) {
				return false;
			}
		}

		return true;
	}

	public List<OrderItem> getItems() {
		return Collections.unmodifiableList(new ArrayList<OrderItem>(items.values()));
	}

	public List<CompositeOrderItem> getCompositeOrderItems() {
		return Collections.unmodifiableList(new ArrayList<CompositeOrderItem>(compositeOrderItems.values()));
	}

	public List<SkuOrderItem> getSkuItems() {
		return Collections.unmodifiableList(new ArrayList<SkuOrderItem>(skuItems.values()));
	}

	public double getTotalWeight() {
		double totalWeight = 0;
		for (SkuOrderItem skuItem : getSkuItems()) {
			totalWeight += skuItem.getSku().getParentProduct().getWeight();
		}
		return totalWeight;
	}

}
