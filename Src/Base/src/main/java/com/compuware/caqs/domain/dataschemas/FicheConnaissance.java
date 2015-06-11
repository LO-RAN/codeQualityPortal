/*
 * FicheConnaissance.java
 *
 * Created on 31 octobre 2002, 11:35
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;

/** Fiche de Connaissance
 *
 * @author cwfr-sbedu
 */
public class FicheConnaissance implements Serializable {
    
    private static final long serialVersionUID = 8275078256530735343L;

	private String idFiche;
    
    private String libelle;
    
    private String description;
    
    private String summary;
    
    private String dialecte;
    
    private java.util.Date dateInst;
    
    private java.util.Date dateMaj;
    
    private String attachement;
    
    private String refTrackRecord;
    
    private String url;
    
    // déclaration du logger
    static protected Logger log = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
    /** Creation d'une nouvelle instance de FicheConnaissance
     */
    public FicheConnaissance() {
    }
    
    
    public void setIdFiche(java.lang.String idFiche){
        this.idFiche=idFiche;
    }
    
    /** Recupere l'ID de la fiche
     * @return Id de la fiche
     */
    public java.lang.String getIdFiche(){
        return this.idFiche;
    }
    
    public void setDialecte(java.lang.String dialecte){
        this.dialecte=dialecte;
    }
    
    /** Recupere le Dialecte de la Fiche
     * @return Dialecte
     */
    public java.lang.String getDialecte(){
        return this.dialecte;
    }
    
    public void setLibelle(java.lang.String libFiche){
        this.libelle=libFiche;
    }
    
    /** Recupere le Libelle (nom court) de la fiche
     * @return Libelle
     */
    public java.lang.String getLibelle(){
        return this.libelle;
    }
    
    public void setDescription(java.lang.String descFiche){
        this.description=descFiche;
    }
    
    /** Recupere la description (nom long) de la fiche
     * @return Description
     */
    public java.lang.String getDescription(){
        return this.description;
    }
    
    public void setSummary(java.lang.String summary){
        this.summary=summary;
    }
    
    /** Recupere le texte de la fiche
     * @return Texte de la fiche
     */
    public java.lang.String getSummary(){
        return this.summary;
    }
    
    public void setDateInst(java.util.Date dateInst){
        this.dateInst=dateInst;
    }
    
    /** Recupere la date d'instanciation de la fiche
     * @return Date d'instanciation
     */
    public java.util.Date getDateInst(){
        return this.dateInst;
    }
    
    public void setDateMaj(java.util.Date dateMaj){
        this.dateMaj=dateMaj;
    }
    
    /** Recupere la date de Mise à jour de la fiche
     * @return Date de Mise
     */
    public java.util.Date getDateMaj(){
        return this.dateMaj;
    }
    
    public void setRefTrackRecord(java.lang.String refTrackRecord){
        this.refTrackRecord=refTrackRecord;
    }
    
    /** Recupere la reference TrackRecord de la fiche
     * @return Reference TrackRecord
     */
    public java.lang.String getRefTrackRecord(){
        return this.refTrackRecord;
    }
    
    public void setUrl(java.lang.String lurl){
        this.url=lurl;
    }
    
    /** Recupere l'url de la fiche
     * @return String url
     */
    public java.lang.String getUrl(){
        return this.url;
    }
    
    public void setAttachement(java.lang.String attachementName){
        this.attachement=attachementName;
    }
    
    public String getAttachement(){
        return this.attachement;
    }
    
}
