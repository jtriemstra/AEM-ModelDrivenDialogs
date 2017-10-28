package com.testprojects.autodialog.example;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.testprojects.autodialog.FieldType;
import com.testprojects.autodialog.annotations.AutoDialogField;

@Model(adaptables = Resource.class)
public class CompositeComponentModel {
	
	@Inject @Optional
    @AutoDialogField
    private String headerText;
	
	@Inject @Optional
	@AutoDialogField(fieldResourceType=FieldType.CONTAINER)
	private LinkModel link1;
	
	@Inject @Optional
	@AutoDialogField(fieldResourceType=FieldType.CONTAINER)
	private LinkModel link2;
	
	public String getHeaderText() {return headerText;}
	public LinkModel getLink1() {return link1;}
	public LinkModel getLink2() {return link2;}
	
}
