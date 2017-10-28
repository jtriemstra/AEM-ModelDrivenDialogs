package com.testprojects.autodialog;

import com.testprojects.autodialog.example.*;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component(immediate = true, metatype = false)
public class AutoDialogModelFinder implements IModelFinder {
	private Logger logger = LoggerFactory.getLogger(AutoDialogModelFinder.class);
	
	@Reference
	private IDialogResourceMap m_objDialogResourceMap;
	
	@Override
	public Class getClass(Resource objResourceInput)
	{
		logger.info("checking resource for: " + objResourceInput.getPath());
		
		return getClassByDialogAttribute(objResourceInput);
	}
	
	private Class getClassByDialogAttribute(Resource objResourceInput)
	{
		ValueMap objValues = objResourceInput.adaptTo(ValueMap.class);
		String strClassName = objValues.get("autoDialogModelClass", String.class);
		try
		{
			return Class.forName(strClassName);
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
	}
	
	private Class getClassByModelFactory(Resource objResourceInput)
	{
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
				//TODO: revert this to modelFactory for AEM 6.2+
				objReturn = objContent.adaptTo(CompositeComponentModel.class);
			}
			catch (Exception e)
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
