package com.testprojects.autodialog;

import java.util.HashMap;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Service
@Component(immediate = true, metatype = false)
public class DialogResourceMap implements IDialogResourceMap {
	private HashMap<String, String> m_hshMap = new HashMap<String, String>();
	
	@Override
	public void put(String strDialogPath, String strResourcePath) {
		m_hshMap.put(strDialogPath, strResourcePath);
	}

	@Override
	public String getResource(String strDialogPath) {
		return m_hshMap.get(strDialogPath);
	}

}
