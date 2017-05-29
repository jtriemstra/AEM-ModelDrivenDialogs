package com.testprojects.autodialog.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.testprojects.autodialog.FieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoDialogField {
	static final String NO_LABEL = "";
	static final String NO_TYPE = "";
	
	boolean skip() default false;	
	String fieldLabel() default NO_LABEL;
	FieldType fieldResourceType() default FieldType.TEXT;
}
