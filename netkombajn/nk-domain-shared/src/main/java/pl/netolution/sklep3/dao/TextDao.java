package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Text;

public interface TextDao extends BaseDao<Text> {

	Text findTextByName(String name);
}
