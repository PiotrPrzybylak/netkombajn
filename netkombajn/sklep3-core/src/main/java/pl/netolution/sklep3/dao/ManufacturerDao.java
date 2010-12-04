package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.Manufacturer;

public interface ManufacturerDao extends BaseDao<Manufacturer> {

	public static final String NAME = "name";

	Manufacturer findByName(String name);
}
