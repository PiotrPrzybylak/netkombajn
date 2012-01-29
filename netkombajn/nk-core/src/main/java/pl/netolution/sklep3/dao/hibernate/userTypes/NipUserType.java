package pl.netolution.sklep3.dao.hibernate.userTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import pl.netolution.sklep3.domain.Nip;

public class NipUserType extends AbstractUserType implements UserType {

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		String nip = rs.getString(names[0]);

		if (rs.wasNull())
			return null;

		if (nip == null)
			return null;

		return new Nip(nip);

	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Hibernate.STRING.sqlType());
		} else {
			Nip nip = (Nip) value;

			statement.setString(index, nip.getCanonicalForm());
		}

	}

	public Class<Nip> returnedClass() {
		return Nip.class;
	}

	public int[] sqlTypes() {
		return new int[] { Hibernate.STRING.sqlType() };
	}

}
