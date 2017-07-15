package com.testprojects.autodialog;

import java.lang.reflect.Field;
import java.util.HashMap;
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
				logger.info("field: " + f.getName());
				Map<String, Object> hshProperties = null;
				
				AutoDialogField objAnnotation = f.getAnnotation(com.testprojects.autodialog.annotations.AutoDialogField.class);
				if (objAnnotation != null && !objAnnotation.skip())
				{
					hshProperties = getDialogProperties(f.getName(), f.getType(), objAnnotation);
				}
				else if (f.getAnnotation(javax.inject.Inject.class) != null)
				{
					if (!fieldAlreadyExistsOnDialog(f.getName()))
					{
						hshProperties = getDialogProperties(f.getName(), f.getType());
					}
				}
				
				//TODO: the node name should probably be an optional field on the annotation
				if (objItemsRoot.getChild(f.getName()) == null)
				{
					objResolver.create(objItemsRoot, f.getName(), hshProperties);
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
	
	private Map<String, Object> getDialogProperties(String strFieldName, Class objFieldClass, AutoDialogField objAnnotation) throws Exception
	{
		Map<String, Object> hshPropertyMap = new HashMap<String, Object>();
		
		hshPropertyMap.put("name", "./" + strFieldName);
		
		if (AutoDialogField.NO_LABEL.equals(objAnnotation.fieldLabel()))
		{
			hshPropertyMap.put("fieldLabel", getFieldLabelByName(strFieldName));
		}
		else 
		{
			hshPropertyMap.put("fieldLabel", objAnnotation.fieldLabel());
		}
		
		if (AutoDialogField.NO_TYPE.equals(objAnnotation.fieldResourceType()))
		{
			hshPropertyMap.put("sling:resourceType", getFieldResourceType(getFieldType(objFieldClass)));
		}
		else
		{			
			hshPropertyMap.put("sling:resourceType", getFieldResourceType(objAnnotation.fieldResourceType()));
		}
		
		return hshPropertyMap;
	}
	
	private Map<String, Object> getDialogProperties(String strFieldName, Class objFieldClass) throws Exception
	{
		Map<String, Object> hshPropertyMap = new HashMap<String, Object>();
		hshPropertyMap.put("fieldLabel", getFieldLabelByName(strFieldName));
		hshPropertyMap.put("name", "./" + strFieldName);
		hshPropertyMap.put("sling:resourceType", getFieldResourceType(getFieldType(objFieldClass)));
		
		return hshPropertyMap;
	}
	
	private boolean fieldAlreadyExistsOnDialog(String strFieldName)
	{
		//TODO: implement real logic to find properties already on the dialog, possibly an XPath query?
		if ("authoredText".equals(strFieldName) || "authoredPath".equals(strFieldName))
		{
			return true;
		}
		return false;
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
		default:
			throw new Exception("field type unknown");
		}
	}
	
	private FieldType getFieldType(Class objFieldClass)
	{
		if (objFieldClass == java.lang.String.class)
		{
			return FieldType.TEXT;
		}
		
		return FieldType.TEXT;
	}
}
