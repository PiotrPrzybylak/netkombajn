package pl.netolution.sklep3.dao.hibernate.userTypes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import pl.netolution.sklep3.domain.payment.Money;

public class MoneyUserType extends AbstractUserType implements UserType {

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
		BigDecimal money = resultSet.getBigDecimal(names[0]);
		// Deferred check after first read
		if (resultSet.wasNull())
			return null;

		if (money == null)
			return null;

		return new Money(money);
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Hibernate.BIG_DECIMAL.sqlType());
		} else {
			Money money = (Money) value;
			statement.setBigDecimal(index, money.getAmount());
		}
	}

	public Class<Money> returnedClass() {
		return Money.class;
	}

	public int[] sqlTypes() {
		return new int[] { Hibernate.BIG_DECIMAL.sqlType() };
	}

}
