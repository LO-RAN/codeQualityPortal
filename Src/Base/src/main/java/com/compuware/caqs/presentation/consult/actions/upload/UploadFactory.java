package com.compuware.caqs.presentation.consult.actions.upload;

import java.io.File;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.upload.Separator;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.actions.upload.uploaders.AbstractCaqsUploader;
import com.compuware.caqs.presentation.consult.actions.upload.uploaders.DefaultCsvMetricsUploader;
import com.compuware.caqs.presentation.consult.forms.UploadDataForm;

import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-dzysman
 */
public class UploadFactory {

    /** The logger. */
	private static final Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    /** The single instance of the class. */
    private static UploadFactory singleton = new UploadFactory();

    /**
     * Private constructor for the singleton.
     */
	private UploadFactory() {
	}

	/**
	 * Get an instance of the factory.
	 * @return an instance of the factory.
	 */
	public static UploadFactory getInstance() {
		return singleton;
	}

	/**
	 * Create a new uploader from the given file and parameters.
	 * @param f the uploaded file.
     * @param form the form with upload parameters.
     * @return a valid uploader, or <code>null</code> if no uploader can be created.
     * @throws CaqsException if the file does not match the uploader specifications.
	 */
	public AbstractCaqsUploader create(File f, UploadDataForm form) throws CaqsException {
		AbstractCaqsUploader result = null;
        BusinessFactory beanFactory = BusinessFactory.getInstance();
        result = (AbstractCaqsUploader)beanFactory.getBean(form.getFileType());
        if (result != null) {
            result.setMaster(form.getMaster());
            if (result instanceof DefaultCsvMetricsUploader) {
                Separator s = Separator.fromId(form.getCsvSeparator());
                ((DefaultCsvMetricsUploader)result).setCsvSeparator(""+s.getCharSeparator());
            }
            if (result.isCorrectExport(f)) {
                result.setUploadedFile(f);
            }
            else {
                throw new CaqsException("caqs.upload.error.wrongfileformat", "Upload error: wrong file format");
            }
        }
        else {
            throw new CaqsException("caqs.upload.error.nouploaderfound", "Upload error: no uploader found");
        }
        return result;
	}

}
