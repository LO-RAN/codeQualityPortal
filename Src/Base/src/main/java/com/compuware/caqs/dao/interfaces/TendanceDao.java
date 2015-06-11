package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;

import com.compuware.caqs.exception.DataAccessException;

public interface TendanceDao {

    /**
     * Mise a jour des tendances pour les criteres.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    public void updateCriterionTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException;

    /**
     * Mise a jour des tendances pour les metriques.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    public void updateMetricTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException;

    /**
     * Mise a jour des tendances pour les objectifs.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    public void updateFactorTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException;

    /**
     * Mise a jour de toutes les tendances d'une ea
     * @param idElt id ea
     * @param idCurrentBline id bline
     * @param idPreviousBline id precedente bline
     */
    public void updateAllTrends(String idElt, String idCurrentBline, String idPreviousBline);

    /**
     * mise a 0 de toutes les tendances pour une ea
     * @param idElt id ea
     * @param idCurrentBline id bline
     */
    public void deleteAllTrends(String idElt, String idCurrentBline);
}
