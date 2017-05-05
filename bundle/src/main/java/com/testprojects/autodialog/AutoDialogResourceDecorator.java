package com.testprojects.autodialog;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceDecorator;
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
			logger.error(objResourceInput.getPath() + " " + objResourceInput.getResourceType());			
		}

		if (this.accepts(objResourceInput))
		{
			//TODO: this class shouldn't know about the packages using it
//			Reflections reflections = new Reflections("com.testprojects.autodialog.example");
//			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(com.testprojects.autodialog.annotations.AutoDialog.class);
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
		if ("cq/gui/components/authoring/dialog".equals(objResourceInput.getResourceType()))
		{
			return true;
		}
		return false;
	
	}
}
