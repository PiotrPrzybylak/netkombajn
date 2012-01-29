package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.enums.ImageFormatName;

public interface ImageFormatDao extends BaseDao<ImageFormat> {

	ImageFormat getByName(ImageFormatName imageFormatName);

}
