package com.testprojects.autodialog.example;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.testprojects.autodialog.FieldType;
import com.testprojects.autodialog.annotations.*;

@Model(adaptables = Resource.class, resourceType="auto-dialog/components/content/simpleComponent")
public class SimpleComponentModel {

	@Inject	
    private String authoredText;
	
    @Inject
    private String authoredPath;
    
    @AutoDialogField(skip=true)
    private String nonAuthoredField;
    
    @Inject
    @Optional
    @AutoDialogField
    private String injectedText;
    
    @Inject
    @Optional
    @AutoDialogField(fieldResourceType=FieldType.PATH)
    private String injectedPath;
    
    public String getAuthoredText(){ return authoredText; }
    
    public String getAuthoredPath(){ return authoredPath; }
    
    public String getInjectedText(){ return injectedText; }
    
    public String getInjectedPath(){ return injectedPath; }
}
