package com.testprojects.autodialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceDecorator;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.felix.scr.annotations.Reference;
//import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Component(immediate = true, metatype = false)
public class AutoDialogResourceDecorator implements ResourceDecorator {

	private Logger logger = LoggerFactory.getLogger(AutoDialogResourceDecorator.class);
	private static final String TOUCH_DIALOG_TYPE = "cq/gui/components/authoring/dialog";
	
	@Reference
	private IModelFinder m_objFinder;
	
	@Override
	public Resource decorate(final Resource objResourceInput) {
		
		if (this.accepts(objResourceInput))
		{
			logger.info("accepted decorator");
			Class objComponentModelClass = m_objFinder.getClass(objResourceInput);
			
			if (objComponentModelClass != null)
			{
				logger.info("model class is: " + objComponentModelClass.getName());
				AutoDialogBuilder objBuilder = new AutoDialogBuilder(objComponentModelClass, objResourceInput);
				return objBuilder.addDynamicFields();
			}			
		}
		else
		{
			//logger.error("rejected decorator");
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
		if (TOUCH_DIALOG_TYPE.equals(objResourceInput.getResourceType()))
		{
			return true;
		}
		return false;
	
	}
	
}
