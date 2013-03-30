package pl.netolution.sklep3.web.jsf.shipment;

import org.apache.commons.lang.StringUtils;

import pl.netolution.sklep3.web.jsf.GenericEntityBackingBean;
import pl.netolution.sklep3.web.jsf.utils.MessageHelper;

import com.netkombajn.eshop.ordering.shipment.ShipmentOption;

public class ShipmentOptionBackingBean extends GenericEntityBackingBean<ShipmentOption> {

	private MessageHelper messageHelper;

	@Override
	public void addEntity() {

		ShipmentOption entity = getAddedEntity();
		String minimalPriceFieldClientId = "newEntityForm:minimalOrderPrice";

		if (validatePriceRange(entity, minimalPriceFieldClientId) && validateWeight(entity)) {
			super.addEntity();
		}

	}

	@Override
	public void updateEntity() {
		ShipmentOption entity = getEditedEntity();
		String minimalPriceFieldClientId = "editEntityForm:minimalOrderPrice";

		if (validatePriceRange(entity, minimalPriceFieldClientId) && validateWeight(entity)) {
			super.updateEntity();
		}

	}

	private boolean validatePriceRange(ShipmentOption entity, String minimalPriceFieldClientId) {
		if (entity.isUseRange()) {
			boolean maximumNotNull = entity.getMaximalOrderPrice() != null;
			boolean minimumNotNull = entity.getMinimalOrderPrice() != null;

			if (maximumNotNull && minimumNotNull) {
				boolean isMaximumLessThanMinimum = entity.getMaximalOrderPrice().compareTo(entity.getMinimalOrderPrice()) < 1;
				if (isMaximumLessThanMinimum) {

					messageHelper.addMessageToContext(minimalPriceFieldClientId, "cena maksymalna jest mniejsza od minimalnej");
					return false;
				}
			}

		}

		return true;
	}

	private boolean validateWeight(ShipmentOption entity) {
		if (StringUtils.isNotEmpty(entity.getWeight())) {
			String[] weights = entity.getWeight().split(";");
			if (weights.length < 0) {
				messageHelper.addMessageToContext(entity.getWeight(), "Wagi powinny być oddzielone średnikami np: 1-20;5-25");
				return false;

			}
			for (String weight : weights) {
				//TODO moze sprawdzac czy wartosci sa liczbami?
				String[] weightPrice = weight.split("-");
				if (weightPrice.length != 2) {
					messageHelper.addMessageToContext(entity.getWeight(), "Waga powinna być oddzielona od ceny myślnikiem np: 1-20");
					return false;
				}
				try{
				Float.parseFloat(weightPrice[0]);
					Float.parseFloat(weightPrice[1]);
				} catch (Exception e) {
					messageHelper.addMessageToContext(entity.getWeight(), "Waga powinna zawierać liczby oddzielone myślnikiem np: 1-20.5");
					return false;
				}
			}
		}
		return true;
	}

	public void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

}
