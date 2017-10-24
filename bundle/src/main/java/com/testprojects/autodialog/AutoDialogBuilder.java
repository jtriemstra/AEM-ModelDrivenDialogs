package com.testprojects.autodialog;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testprojects.autodialog.annotations.AutoDialogField;

public class AutoDialogBuilder {
	private Logger logger = LoggerFactory.getLogger(AutoDialogBuilder.class);
	private Class m_objComponentModelClass;
	private Resource m_objResourceInput;
	
	public AutoDialogBuilder(Class objComponentModelClass, Resource objResourceInput)
	{
		m_objComponentModelClass = objComponentModelClass;
		m_objResourceInput = objResourceInput;
	}
	
	public Resource addDynamicFields()
	{
		ResourceResolver objResolver = m_objResourceInput.getResourceResolver();
		//TODO: this needs to handle dialogs that don't exist at all, and also situations where we don't want to mess with tab1
		Resource objItemsRoot = m_objResourceInput.getChild("content/items/tab1/items/columns/items");
		
		try{
			Field[] objFields = m_objComponentModelClass.getDeclaredFields();
			for(Field f : objFields)
			{
				logger.debug("field: " + f.getName());
				String strFieldName = "";
				
				AutoDialogField objAnnotation = f.getAnnotation(com.testprojects.autodialog.annotations.AutoDialogField.class);
				if (objAnnotation != null && !objAnnotation.skip())
				{
					if (!AutoDialogField.NO_NAME.equals(objAnnotation.fieldName()))
					{
						strFieldName = objAnnotation.fieldName();
					}
					else
					{
						strFieldName = f.getName();
					}
					
					if (objItemsRoot.getChild(strFieldName) == null)
					{
						Map<String, Object> hshProperties = getDialogProperties(strFieldName, f.getType(), objAnnotation);
						Resource objFieldResource = objResolver.create(objItemsRoot, strFieldName, hshProperties);
						
						putDialogFieldChildren(objFieldResource, objResolver, strFieldName, f.getType(), objAnnotation);
					}
					else
					{
						logger.warn("skipping auto-generated dialog fields on " + strFieldName + " because some are already defined");
					}
				}
			}
			
			return m_objResourceInput;
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			return m_objResourceInput;
		}
	}
	
	private void putDialogFieldChildren(Resource objFieldResource, ResourceResolver objResolver, String strFieldName, Class objFieldClass, AutoDialogField objAnnotation) throws Exception
	{
		FieldType objFieldType;
		
		if (FieldType.IMPLICIT == objAnnotation.fieldResourceType())
		{
			objFieldType = getFieldType(objFieldClass);
		}
		else
		{			
			objFieldType = objAnnotation.fieldResourceType();
		}
		
		switch(objFieldType)
		{
		case DROPDOWN:
			putDropdownFieldChildren(objFieldResource, objResolver, strFieldName, objFieldClass, objAnnotation);
			break;
		default:
			break;
		}
	}
	
	private void putDropdownFieldChildren(Resource objFieldResource, ResourceResolver objResolver, String strFieldName, Class objFieldClass, AutoDialogField objAnnotation) throws Exception
	{
		Resource objItems = objResolver.create(objFieldResource, "items", null);
		
		for (Object objEnumValue : objFieldClass.getEnumConstants())
		{
			Map<String, Object> hshProperties = new HashMap<String, Object>();
			hshProperties.put("text", ((Enum) objEnumValue).name());
			
			Resource objItem = objResolver.create(objItems, ((Enum) objEnumValue).name(), hshProperties);
		}
	}
	
	private Map<String, Object> getDialogProperties(String strFieldName, Class objFieldClass, AutoDialogField objAnnotation) throws Exception
	{
		Map<String, Object> hshPropertyMap = new HashMap<String, Object>();
		String strLabelPropertyName = "";
		FieldType objFieldType;
		
		//TODO: make name and optional annotation property?
		hshPropertyMap.put("name", "./" + strFieldName);
		
		if (FieldType.IMPLICIT == objAnnotation.fieldResourceType())
		{
			objFieldType = getFieldType(objFieldClass);
		}
		else
		{			
			objFieldType = objAnnotation.fieldResourceType();
		}
		
		hshPropertyMap.put("sling:resourceType", getFieldResourceType(objFieldType));
		strLabelPropertyName = getLabelPropertyName(objFieldType);
		
		if (AutoDialogField.NO_LABEL.equals(objAnnotation.fieldLabel()))
		{
			hshPropertyMap.put(strLabelPropertyName, getFieldLabelByName(strFieldName));
		}
		else 
		{
			hshPropertyMap.put(strLabelPropertyName, objAnnotation.fieldLabel());
		}
		
		return hshPropertyMap;
	}
	
	private String getLabelPropertyName(FieldType objFieldType)
	{
		if (objFieldType == FieldType.CHECKBOX) return "text";
		return "fieldLabel";
	}
		
	private String getFieldLabelByName(String strFieldName)
	{
		//TODO: programmatically change casing and add spaces
		if ("injectedText".equals(strFieldName))
		{
			return "Injected Text";
		}
		return strFieldName;
	}
	
	private String getFieldResourceType(FieldType objFieldType) throws Exception
	{
		switch (objFieldType)
		{
		case TEXT: 
			return "granite/ui/components/foundation/form/textfield";
		case PATH:
			return "granite/ui/components/foundation/form/pathbrowser";
		case DROPDOWN:
			return "granite/ui/components/foundation/form/select";
		case CHECKBOX:
			return "granite/ui/components/foundation/form/checkbox";
		default:
			throw new Exception("field type unknown");
		}
	}
	
	private FieldType getFieldType(Class objFieldClass)
	{
		logger.info("getting field type for: " + objFieldClass.getName());
		
		if (objFieldClass.isEnum())
		{
			return FieldType.DROPDOWN;
		}
		
		if (objFieldClass.equals(boolean.class))
		{
			return FieldType.CHECKBOX;
		}
		
		return FieldType.TEXT;
	}
}
