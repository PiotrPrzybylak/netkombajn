package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.enums.ImageFormatName;

public interface ImageFormatDao extends BaseDao<ImageFormat> {

	ImageFormat getByName(ImageFormatName imageFormatName);

}
