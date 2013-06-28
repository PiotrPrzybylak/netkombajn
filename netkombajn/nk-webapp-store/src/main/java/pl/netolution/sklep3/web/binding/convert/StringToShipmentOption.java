package pl.netolution.sklep3.web.binding.convert;

import org.springframework.binding.convert.converters.StringToObject;

import com.netkombajn.eshop.ordering.shipment.ShipmentOption;
import com.netkombajn.eshop.ordering.shipment.ShipmentOptionDao;


public class StringToShipmentOption extends StringToObject {

	private ShipmentOptionDao shipmentOptionDao;

	public StringToShipmentOption() {
		super(ShipmentOption.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object toObject(String string, Class targetClass) throws Exception {
		ShipmentOption shipmentOption = shipmentOptionDao.findById(Long.valueOf(string));
		return shipmentOption;
	}

	@Override
	protected String toString(Object object) throws Exception {
		if (object == null) {
			return null;
		}
		return ((ShipmentOption) object).getId().toString();
	}

	public void setShipmentOptionDao(ShipmentOptionDao shipmentOptionDao) {
		this.shipmentOptionDao = shipmentOptionDao;
	}
}
