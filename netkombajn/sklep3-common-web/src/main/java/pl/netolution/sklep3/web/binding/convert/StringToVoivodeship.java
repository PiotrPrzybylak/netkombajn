package pl.netolution.sklep3.web.binding.convert;

import org.apache.log4j.Logger;
import org.springframework.binding.convert.converters.StringToObject;

import pl.netolution.sklep3.dao.VoivodeshipDao;
import pl.netolution.sklep3.domain.Voivodeship;

public class StringToVoivodeship extends StringToObject {

	private static final Logger log = Logger.getLogger(StringToVoivodeship.class);

	private VoivodeshipDao voivodeshipDao;

	public StringToVoivodeship() {
		super(Voivodeship.class);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object toObject(String string, Class targetClass) {
		log.debug("Converting String to Voivodeship: " + string);
		Voivodeship voivodeship = voivodeshipDao.findById(Long.valueOf(string));
		log.debug("Converted value: " + voivodeship);
		return voivodeship;
	}

	@Override
	protected String toString(Object object) throws Exception {
		log.debug("Converting Voivodeship to String: " + object);
		if (object == null) {
			return null;
		}
		return ((Voivodeship) object).getId().toString();
	}

	public void setVoivodeshipDao(VoivodeshipDao voivodeshipDao) {
		this.voivodeshipDao = voivodeshipDao;
	}

}
