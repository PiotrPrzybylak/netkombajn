package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.Text;

public interface TextDao extends BaseDao<Text> {

	Text findTextByName(String name);
}
