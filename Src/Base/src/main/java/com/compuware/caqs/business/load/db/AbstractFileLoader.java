package com.compuware.caqs.business.load.db;


import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractFileLoader extends DataLoader implements FileLoader {

    //logger
    static private Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    public AbstractFileLoader(ElementBean superElement, ProjectBean projectBean, BaselineBean baselineBean) {
        super(superElement, projectBean, baselineBean);
	}

	/**
	 * Load the data from the given DataFile.
	 * @param file the XML result file.
	 */
    public void load(DataFile file) throws LoaderException {
    	logger.debug("Loading:"+file.getPath());
		if (file != null) {
			File f = new File(file.getPath());
			loadEltData(f);
		}
	}

    /**
     * Load the data from the given DataFile list.
	 * @param fileList a list of XML result file.
     */
	public void load(List<DataFile> fileList) throws LoaderException {
		if (fileList != null) {
			DataFile f = null;
			Iterator<DataFile> i = fileList.iterator();
			while (i.hasNext()) {
				f = i.next();
				load(f);
			}
		}
	}

    protected abstract void loadEltData(File f) throws LoaderException;
}
