package com.testprojects.autodialog;

public interface IDialogResourceMap {
	public void put(String strDialogPath, String strResourcePath);
	public String getResource(String strDialogPath);
}
