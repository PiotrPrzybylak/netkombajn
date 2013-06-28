package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Manufacturer;

public interface ManufacturerDao extends BaseDao<Manufacturer> {

	public static final String NAME = "name";

	Manufacturer findByName(String name);
}
