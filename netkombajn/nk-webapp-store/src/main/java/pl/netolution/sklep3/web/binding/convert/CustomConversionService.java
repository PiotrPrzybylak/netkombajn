package pl.netolution.sklep3.web.binding.convert;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.convert.service.DefaultConversionService;

public class CustomConversionService extends DefaultConversionService implements InitializingBean {

	private static final Logger log = Logger.getLogger(CustomConversionService.class);

	private StringToVoivodeship stringToVoivodeship;

	private StringToShipmentOption stringToShipmentOption;

	public void addCustomConverters() {
		addConverter("nip", new StringToNip());
		// addConverter("voivodeship", stringToVoivodeship);
		log.debug("Adding converter: " + stringToVoivodeship);
		addConverter(stringToVoivodeship);
		addConverter(stringToShipmentOption);

	}

	public void setStringToVoivodeship(StringToVoivodeship stringToVoivodeship) {
		this.stringToVoivodeship = stringToVoivodeship;
	}

	public void setStringToShipmentOption(StringToShipmentOption stringToShipmentOption) {
		this.stringToShipmentOption = stringToShipmentOption;
	}

	public void afterPropertiesSet() {
		addCustomConverters();

	}

}
