package com.uni.lu.micseventmanager.dao;

import com.google.android.gms.tasks.Task;

public interface IDAO<T> {

	Task<Void> save(T pojo);
	void delete(T pojo);
	Task<Void> update(T pojo);

}
