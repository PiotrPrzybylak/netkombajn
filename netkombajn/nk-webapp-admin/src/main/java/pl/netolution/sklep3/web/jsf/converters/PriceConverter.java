package pl.netolution.sklep3.web.jsf.converters;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import pl.netolution.sklep3.domain.Price;

public class PriceConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		BigDecimal priceAmount;

		if (value == null || value.equals("")) {
			return null;
		}

		try {
			priceAmount = new BigDecimal(value.replaceAll(",", "."));
		} catch (NumberFormatException e) {
			throw new ConverterException(new FacesMessage("Niepoprawna wartość. Przykładowa wartość to 12,45"));
		}
		Price price = new Price(priceAmount);

		return price;
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {

		if (object == null) {
			return "";
		}

		Price price = (Price) object;

		if (price.getValue() == null) {
			return "";
		}

		return String.format("%.2f", price.getValue());

	}

}
