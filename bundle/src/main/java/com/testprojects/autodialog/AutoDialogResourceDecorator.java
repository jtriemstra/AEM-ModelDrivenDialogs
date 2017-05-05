package com.testprojects.autodialog;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceDecorator;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
//import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Component(immediate = true, metatype = false)
public class AutoDialogResourceDecorator implements ResourceDecorator {

	private Logger logger = LoggerFactory.getLogger(AutoDialogResourceDecorator.class);
	
	@Override
	public Resource decorate(final Resource objResourceInput) {
		
		if (objResourceInput != null)
		{
						
		}

		if (this.accepts(objResourceInput))
		{
			logger.error(objResourceInput.getPath() + " " + objResourceInput.getResourceType());
			
			Resource objItemsRoot = objResourceInput.getChild("content/items/tab1/items/columns/items");
			ResourceResolver objResolver = objItemsRoot.getResourceResolver();
			
			/*objResolver.create(objItemsRoot, "injectedText", null);
			
			ValueMap objMap = objItemsRoot.adaptTo(ValueMap.class);
			for(Object objKey : objMap.keySet())
			{
				logger.error("Key is " + objKey.toString());
				logger.error("Value is " + objMap.get(objKey).toString());
			}*/
			
		}
		
	    return objResourceInput;
	    
	}
	
	public Resource decorate(Resource resource, HttpServletRequest request) {
	    return this.decorate(resource);
	}
	
	private boolean accepts(final Resource objResourceInput)
	{
		//NOTE from ACS Commons
		// Note: If you are checking if a resource should be decorated based on resource type,
        // Using ResourceUtil.isA(..) will send this into an infinite recursive lookup loop
		// Joel is finding this appears to be true of objResourceInput.isResourceType() as well
		if ("cq/gui/components/authoring/dialog".equals(objResourceInput.getResourceType())
				&& "/apps/autodialog".equals(objResourceInput.getPath().substring(0, 16)))
		{
			return true;
		}
		return false;
	
	}
}
