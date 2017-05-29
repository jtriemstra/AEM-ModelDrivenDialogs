package com.testprojects.autodialog.example;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import com.testprojects.autodialog.FieldType;
import com.testprojects.autodialog.annotations.*;

//NOTE: I don't love this string, but I don't see a way around it once AutoDialogModelFinder gets fixed
@AutoDialog(resourceType="/apps/auto-dialog/components/content/simpleComponent")
@Model(adaptables = Resource.class)
public class SimpleComponentModel {

	@Inject	
    private String authoredText;
	
    @Inject
    private String authoredPath;
    
    @AutoDialogField(skip=true)
    private String nonAuthoredField;
    
    @Inject
    private String injectedText;
    
    @Inject
    @AutoDialogField(fieldResourceType=FieldType.PATH)
    private String injectedPath;
    
    public String getAuthoredText(){ return authoredText; }
    
    public String getAuthoredPath(){ return authoredPath; }
    
    public String getInjectedText(){ return injectedText; }
    
    public String getInjectedPath(){ return injectedPath; }
}
