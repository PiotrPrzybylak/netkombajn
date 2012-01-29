package pl.netolution.sklep3.web.jsf;

import java.lang.reflect.ParameterizedType;

import pl.netolution.sklep3.dao.BaseDao;

public class GenericEntityBackingBean<T> {

	private BaseDao<T> entityDao;

	private T editedEntity;

	private T addedEntity;

	private final Class<T> entityType;

	@SuppressWarnings("unchecked")
	public GenericEntityBackingBean() {
		entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		addedEntity = createTypeInstance();
	}

	public void updateEntity() {
		entityDao.makePersistent(editedEntity);
		editedEntity = null;
	}

	public void addEntity() {
		entityDao.makePersistent(addedEntity);
		addedEntity = createTypeInstance();

	}

	private T createTypeInstance() {
		try {
			return entityType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteEntity() {
		entityDao.delete(editedEntity);
		editedEntity = null;
	}

	public BaseDao<T> getEntityDao() {
		return entityDao;
	}

	public void setEntityDao(BaseDao<T> baseDao) {
		this.entityDao = baseDao;
	}

	public T getEditedEntity() {
		return editedEntity;
	}

	public void setEditedEntity(T editetEntity) {
		this.editedEntity = editetEntity;
	}

	public T getAddedEntity() {
		return addedEntity;
	}

	public void setAddedEntity(T addedEntity) {
		this.addedEntity = addedEntity;
	}
}
