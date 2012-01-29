package pl.netolution.sklep3.dao.hibernate;

import java.util.List;

import pl.netolution.sklep3.dao.PictureDao;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.ProductPicture;

public class HibernatePictureDao extends HibernateBaseDao<Picture> implements PictureDao {

	@SuppressWarnings("unchecked")
	public List<ProductPicture> getAllProductPictures() {
		return getSession().createCriteria(ProductPicture.class).list();
	}
}
