package com.testprojects.autodialog;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.testprojects.autodialog.annotations.AutoDialogField;

public class AutoDialogModelFinder {

	//TODO: replace this with reflection on the @AutoDialog annotation
	public static Class getClass(String strDialogPath)
	{
		if ("/apps/auto-dialog/components/content/simpleComponent/cq:dialog".equals(strDialogPath)){
			return com.testprojects.autodialog.example.SimpleComponentModel.class;
		}
		
		return null;
	}
	
}
