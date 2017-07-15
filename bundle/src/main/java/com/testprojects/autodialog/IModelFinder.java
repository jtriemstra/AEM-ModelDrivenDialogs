package com.testprojects.autodialog;

import org.apache.sling.api.resource.Resource;

public interface IModelFinder {
	public Class getClass(Resource objResourceInput);
}
