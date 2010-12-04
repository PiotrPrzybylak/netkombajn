package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.ImageFormatDao;
import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.enums.ImageFormatName;

public class HibernateImageFormatDao extends HibernateBaseDao<ImageFormat> implements ImageFormatDao {

	public ImageFormat getByName(ImageFormatName imageFormatName) {
		return (ImageFormat) getSession().createCriteria(ImageFormat.class).add(Restrictions.eq("name", imageFormatName)).uniqueResult();
	}

}
