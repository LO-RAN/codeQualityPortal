package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.dataschemas.InternationalizationBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorModelBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.struts.util.MessageResources;

public class ModelSvc {

    private static final ModelSvc instance = new ModelSvc();

    private ModelSvc() {
    }

    public static ModelSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * Retrieve all models from database.
     * @return a model collection.
     */
    public Collection<UsageBean> retrieveAllModels() {
        return DaoFactory.getInstance().getUsageDao().retrieveAllUsages();
    }

    /**
     * Retrieve a model according to an element unique ID.
     * @param idUsa the element unique ID.
     * @return the model found.
     */
    public UsageBean retrieveUsageById(String idUsa) {
        UsageDao dao = DaoFactory.getInstance().getUsageDao();
        return dao.retrieveUsageById(idUsa);
    }

    /**
     * Retrieve models by id
     * @param id the pattern to search for the id
     * @param lib the pattern to search for the lib
     * @param idLoc le language id
     * @return the retrieved models, if any, null otherwise
     */
    public List<UsageBean> retrieveModelsByIdAndLib(String id, String lib, Locale loc) {
        UsageDao dao = DaoFactory.getInstance().getUsageDao();
        List<UsageBean> retour = dao.retrieveModelsByIdAndLib(id, lib, loc.getLanguage());
        return retour;
    }

    /**
     * retrieves a model with the number of eas using it
     * @param id model'id to search for
     * @return model with the number of eas using it, if there is one
     */
    public ModelEditorModelBean retrieveModelWithAssociationCountById(String id) {
        return DaoFactory.getInstance().getUsageDao().retrieveModelWithAssociationCountById(id);
    }

    /**
     * deletes a model
     * @param id the id of the model to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteQualityModel(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getUsageDao().deleteQualityModel(id);
            CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(id);
            if (man != null) {
                man.deleteModel(id);
            }
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * saves a model, updating it if it already exists, creating it otherwise
     * @param model the model to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveModelBean(UsageBean model) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getUsageDao().saveModelBean(model);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * delete a goal association from this model
     * @param modelId model id
     * @param goalId goal to delete id
     * @return no_error if there is no error, the error code otherwise
     */
    public MessagesCodes removeGoalFromModel(String modelId, String goalId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            List<String> l = DaoFactory.getInstance().getUsageDao().removeGoalFromModel(modelId, goalId);
            if (l != null) {
                CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
                if (man != null) {
                    for (String critId : l) {
                        man.removeCritere(critId);
                    }
                    man.saveToDisk();
                }
            }
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * duplicates a model, if it does not already exist
     * @param modelId model to duplicate id
     * @param newModelId new model id
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes duplicateModel(String modelId, String newModelId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if (this.retrieveUsageById(newModelId) == null) {
            try {
                DaoFactory.getInstance().getUsageDao().duplicateModel(modelId, newModelId);
                InternationalizationSvc i18nSvc = InternationalizationSvc.getInstance();
                UsageBean oldModel = new UsageBean(modelId);
                UsageBean newModel = new UsageBean(newModelId);
                Collection<Locale> locales = i18nSvc.retrieveLocales();
                if (locales != null) {
                    InternationalizationBean i18n = null;
                    for (Locale loc : locales) {
                        i18n = new InternationalizationBean();
                        i18n.setTableName(oldModel.getBundleName());
                        i18n.setLanguageId(loc.getLanguage());
                        i18n.setTableId(newModelId);
                        i18n.setColumnName("lib");
                        i18n.setText(oldModel.getLib(loc));
                        if (i18n.getText() == null || "".equals(i18n.getText())) {
                            i18n.setText(newModelId);
                        }
                        retour = i18nSvc.updateData(i18n, newModel);
                        i18n.setColumnName("desc");
                        i18n.setText(oldModel.getDesc(loc));
                        retour = i18nSvc.updateData(i18n, newModel);
                        i18n.setColumnName("compl");
                        i18n.setText(oldModel.getComplement(loc));
                        retour = i18nSvc.updateData(i18n, newModel);
                    }
                }
            } catch (DataAccessException dae) {
                logger.error(dae);
                retour = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour = MessagesCodes.MODEL_DUPLICATION_MODEL_ALREADY_EXISTS;
        }
        return retour;
    }

    /**
     * archive le modele en le dupliquant et assignant la version dupliquee aux eas
     * l'utilisant
     * @param modelId modele a archiver
     * @return
     */
    public MessagesCodes archiveModel(String modelId, MessageResources resources) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            Date now = new Date();
            String newModelId = modelId;
            if(modelId.length()>17) {
                modelId = modelId.substring(0, 17);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            newModelId += "_" + sdf.format(now);
            //caqs.dateFormat.withHour
            Locale fr = new Locale("fr");
            Locale en = new Locale("en");
            SimpleDateFormat frSdf = new SimpleDateFormat(resources.getMessage(fr, "caqs.dateFormat.withHour"));
            SimpleDateFormat enSdf = new SimpleDateFormat(resources.getMessage(en, "caqs.dateFormat.withHour"));
            DaoFactory.getInstance().getUsageDao().duplicateModel(modelId, newModelId);
            InternationalizationSvc i18nSvc = InternationalizationSvc.getInstance();
            UsageBean oldModel = new UsageBean(modelId);
            UsageBean newModel = new UsageBean(newModelId);
            Collection<Locale> locales = i18nSvc.retrieveLocales();
            if (locales != null) {
                InternationalizationBean i18n = null;
                for (Locale loc : locales) {
                    i18n = new InternationalizationBean();
                    i18n.setTableName(oldModel.getBundleName());
                    i18n.setLanguageId(loc.getLanguage());
                    i18n.setTableId(newModelId);
                    i18n.setColumnName("lib");
                    i18n.setText(oldModel.getLib(loc));
                    if (i18n.getText() == null || "".equals(i18n.getText())) {
                        i18n.setText(newModelId);
                    }
                    if ("en".equals(loc.getLanguage())) {
                        i18n.setText(i18n.getText() + " (" + enSdf.format(now)
                                + ")");
                    } else {
                        //sinon, on place la date en formattage francais
                        i18n.setText(i18n.getText() + " (" + frSdf.format(now)
                                + ")");
                    }
                    retour = i18nSvc.updateData(i18n, newModel);
                    i18n.setColumnName("desc");
                    i18n.setText(oldModel.getDesc(loc));
                    retour = i18nSvc.updateData(i18n, newModel);
                    i18n.setColumnName("compl");
                    i18n.setText(oldModel.getComplement(loc));
                    retour = i18nSvc.updateData(i18n, newModel);
                }
            }
            //nous assignons le modele archive aux eas existantes qui l'utilisent
            DaoFactory.getInstance().getUsageDao().archiveModel(modelId, newModelId);
            if (MessagesCodes.NO_ERROR.equals(retour)) {
                retour = CaqsQualimetricModelManager.duplicateModel(modelId, newModelId);
            }
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }

        return retour;
    }
}
