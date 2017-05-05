package com.testprojects.autodialog;

import javax.servlet.ServletRequest;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.SlingRequestEvent;
import org.apache.sling.api.request.SlingRequestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testprojects.autodialog.impl.filters.LoggingFilter;

@Service(org.apache.sling.api.request.SlingRequestListener.class)
@Component(immediate = true, metatype = false)
public class AutoDialogRequestListener implements SlingRequestListener {
	private Logger logger = LoggerFactory.getLogger(AutoDialogRequestListener.class);
	static final String SERVICE_NAME = "org.apache.sling.api.request.SlingRequestListener";

	public void onEvent(SlingRequestEvent objEvent) {
		switch(objEvent.getType()){
		case EVENT_INIT:
			logger.error("joel event listener init");
			break;
		case EVENT_DESTROY:
			logger.error("joel event listener destroy");
			break;
		default:
			logger.error("joel event listener unknown type");
			break;
		}
		
		checkRequest(objEvent.getServletRequest());
		
		Object objStoredResource = objEvent.getServletContext().getAttribute("resource");
		logger.error("resource attr is null: " + (objStoredResource == null));
	}
	
	private void checkRequest(ServletRequest objRequest)
	{
		if (objRequest instanceof SlingHttpServletRequest) logger.error("sling request found via instanceof");
		logger.error("non-sling request");
	}
	
	private void checkRequest(SlingHttpServletRequest objRequest)
	{
		logger.error("sling request");
	}

}
