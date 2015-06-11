package com.compuware.caqs.business.load.db;

import java.util.List;

public interface FileLoader {

	public void setMainTool(boolean mainTool);
	public void load(List<DataFile> fileList) throws LoaderException;
	public void load(DataFile file) throws LoaderException;
	
}
