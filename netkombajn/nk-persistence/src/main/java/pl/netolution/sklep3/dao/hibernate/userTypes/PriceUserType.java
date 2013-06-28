package pl.netolution.sklep3.dao.hibernate.userTypes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.netkombajn.store.domain.shared.price.Price;


public class PriceUserType extends AbstractUserType implements UserType {

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
		BigDecimal price = resultSet.getBigDecimal(names[0]);
		// Deferred check after first read
		if (resultSet.wasNull())
			return null;

		if (price == null)
			return null;

		return new Price(price);
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Hibernate.BIG_DECIMAL.sqlType());
		} else {
			Price price = (Price) value;
			statement.setBigDecimal(index, price.getValue());
		}
	}

	public Class<Price> returnedClass() {
		return Price.class;
	}

	public int[] sqlTypes() {
		return new int[] { Hibernate.BIG_DECIMAL.sqlType() };
	}

}
