package com.kahveciefendi.shop.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * App initializier to bind in Spring {@link AppConfig}, etc.
 * 
 * @author Ayhan Dardagan
 *
 */
public class AppInitializier extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] { AppConfig.class };
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return null;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }

  /**
   * TODO: Forcing UTF-8 encoding when receiving POST parameters (problem when using TOMCAT).
   */
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    // TODO Auto-generated method stub
    super.onStartup(servletContext);
    FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter",
        new CharacterEncodingFilter());
    encodingFilter.setInitParameter("encoding", "UTF-8");
    encodingFilter.setInitParameter("forceEncoding", "true");
    encodingFilter.addMappingForUrlPatterns(null, true, "/*");
  }
}