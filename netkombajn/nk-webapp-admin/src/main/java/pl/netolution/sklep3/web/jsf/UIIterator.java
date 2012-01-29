package pl.netolution.sklep3.web.jsf;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sun.facelets.component.UIRepeat;

public class UIIterator extends UIRepeat {

	private static final Logger log = Logger.getLogger(UIIterator.class);

	@Override
	public Object getValue() {

		Object originalValue = super.getValue();
		log.debug("Original value: " + originalValue);
		if (originalValue instanceof Set) {
			log.debug("Converting Set to List.");
			return new ArrayList<Object>((Set<?>) originalValue);
		}
		log.debug("Returnig originalValue unchanged");
		return originalValue;
	}
}