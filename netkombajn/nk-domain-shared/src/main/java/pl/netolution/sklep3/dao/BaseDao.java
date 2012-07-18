package pl.netolution.sklep3.dao;

import java.util.List;

public interface BaseDao<T> {

	void makePersistent(T entity);

	List<T> getAll();

	T findById(Long id);

	void delete(T entity);

	void refresh(T entity);

	int countAll();

	List<T> getAllSortedBy(String sortProperty);

	void flush();// for tests
}
