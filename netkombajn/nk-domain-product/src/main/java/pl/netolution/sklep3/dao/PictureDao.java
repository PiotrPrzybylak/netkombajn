package pl.netolution.sklep3.dao;

import java.util.List;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.ProductPicture;

public interface PictureDao extends BaseDao<Picture> {

	List<ProductPicture> getAllProductPictures();

}
