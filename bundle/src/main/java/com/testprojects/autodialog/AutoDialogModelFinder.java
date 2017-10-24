package com.testprojects.autodialog;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.testprojects.autodialog.annotations.AutoDialogField;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component(immediate = true, metatype = false)
public class AutoDialogModelFinder implements IModelFinder {
	private Logger logger = LoggerFactory.getLogger(AutoDialogModelFinder.class);
	
	@Reference
	private ModelFactory modelFactory;
	
	@Reference
	private IDialogResourceMap m_objDialogResourceMap;
	
	@Override
	public Class getClass(Resource objResourceInput)
	{
		
		logger.info("checking resource for: " + objResourceInput.getPath());
		
		//TODO: this is not an elegant solution
		String strResource = m_objDialogResourceMap.getResource(objResourceInput.getPath());
		
		Object objReturn = null;
		
		if (strResource != null && !"".equals(strResource))
		{
			logger.info("resource was found: " + strResource);
			ResourceResolver objResolver = objResourceInput.getResourceResolver();
			Resource objContent = objResolver.getResource(strResource);
			try
			{
				objReturn = modelFactory.getModelFromResource(objContent);
			}
			catch (org.apache.sling.models.factory.ModelClassException e)
			{
				logger.info("couldn't create model class for " + strResource, e);
			}
		}
				
		if (objReturn != null)
		{
			logger.error("modelFactory created model");
			return objReturn.getClass();
		}
		else 
		{
			logger.error("modelFactory could not create model");
			return null;
		}
	}
	
}
