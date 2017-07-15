package com.testprojects.autodialog;

import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.Filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

@SlingFilter(order = 0, scope = SlingFilterScope.COMPONENT)
public class DialogRequestInterceptorFilter implements Filter 
{
	private static final Logger log = LoggerFactory.getLogger(DialogRequestInterceptorFilter.class);
	private static final String TOUCH_DIALOG_TYPE = "cq/gui/components/authoring/dialog";
	
	@Reference
	private IDialogResourceMap m_objDialogResourceMap;
	
	@Override
	public void destroy() 
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		log.info("doing filter");
		if (request instanceof SlingHttpServletRequest && accepts((SlingHttpServletRequest) request)) {
			log.info("filter accepted");
			Resource resource = ((SlingHttpServletRequest) request).getResource();
			String strSuffix = ((SlingHttpServletRequest) request).getRequestPathInfo().getSuffix();
			log.info("saving map for: " + resource.getPath().replace("/mnt/override", ""));
			m_objDialogResourceMap.put(resource.getPath().replace("/mnt/override", ""), strSuffix);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException 
	{
	}
	
	private boolean accepts(SlingHttpServletRequest request) 
	{
		if (TOUCH_DIALOG_TYPE.equals(request.getResource().getResourceType()))
		{
			return true;
		}
		return false;
	}
}
