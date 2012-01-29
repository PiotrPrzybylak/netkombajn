package pl.netolution.sklep3.web.binding.convert;

import org.apache.log4j.Logger;
import org.springframework.binding.convert.converters.StringToObject;

import pl.netolution.sklep3.domain.Nip;

public class StringToNip extends StringToObject {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(StringToNip.class);

	public StringToNip() {
		super(Nip.class);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object toObject(String string, Class targetClass) {
		return new Nip(string);
	}

	@Override
	protected String toString(Object object) throws Exception {
		return object.toString();
	}

}
