package com.uni.lu.eventmanager.dao;

public interface IDAO<T> {

	void save(T pojo);
	void delete(T pojo);
	void update(T pojo);

}
