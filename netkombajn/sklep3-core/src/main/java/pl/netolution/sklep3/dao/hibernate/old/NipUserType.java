package pl.netolution.sklep3.dao.hibernate.old;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import pl.netolution.sklep3.domain.Nip;

public class NipUserType implements UserType {
	public int[] sqlTypes() {
		return new int[] { Hibernate.STRING.sqlType() };
	}

	public Class<Nip> returnedClass() {
		return Nip.class;
	}

	public boolean isMutable() {
		return false;
	}

	public Object deepCopy(Object value) {
		return value;
	}

	public Serializable disassemble(Object value) {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	public Object replace(Object original, Object target,

	Object owner) {
		return original;
	}

	public boolean equals(Object x, Object y) {
		if (x == y)
			return true;
		if (x == null || y == null)
			return false;
		return x.equals(y);
	}

	public int hashCode(Object x) {
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws SQLException {
		String nip = resultSet.getString(names[0]);
		// Deferred check after first read
		if (resultSet.wasNull())
			return null;

		return new Nip(nip);
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Hibernate.STRING.sqlType());
		} else {
			statement.setString(index, value.toString());
		}
	}
}