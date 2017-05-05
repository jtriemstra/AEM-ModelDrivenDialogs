package com.testprojects.autodialog.example;

import javax.inject.Inject;
 

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;


@Model(adaptables = Resource.class)
public class SimpleComponentModel {

	@Inject
    private String authoredText;
	
    @Inject
    private String authoredPath;
    
    public String getAuthoredText(){ return authoredText; }
    
    public String getAuthoredPath(){ return authoredPath; }
}
