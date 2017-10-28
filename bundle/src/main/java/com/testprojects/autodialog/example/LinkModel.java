package com.testprojects.autodialog.example;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.testprojects.autodialog.FieldType;
import com.testprojects.autodialog.annotations.AutoDialogField;

@Model(adaptables = Resource.class)
public class LinkModel {
	
	@Inject @Optional
    @AutoDialogField
    private String linkText;
	
    @Inject @Optional
    @AutoDialogField(fieldResourceType=FieldType.PATH)
    private String linkPath;
    
    public String getLinkText(){return linkText;}
    public String getLinkPath(){return linkPath;}
	
}
