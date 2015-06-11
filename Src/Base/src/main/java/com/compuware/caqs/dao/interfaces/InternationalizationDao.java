package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.InternationalizationBean;

public interface InternationalizationDao {

	public abstract void initResources(Locale loc);

	public abstract void initResources(String tableName, Locale loc);

	public abstract Collection<Locale> retrieveLocales();

	public abstract Locale retrieveLocale(String language);

	public abstract void updateData(Collection<InternationalizationBean> data);

	public abstract void updateData(InternationalizationBean bean,
			Connection connection) throws SQLException;

    /**
     * updates i18n data, creating, updating or deleting it.
     * @param data the data to be updated
     */
    public void updateData(InternationalizationBean data) throws SQLException;

}