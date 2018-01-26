package com.kahveciefendi.shop.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * Misc configurations like for Jackson (message converter) and CORS.
 * 
 * @author Ayhan Dardagan
 *
 */
@Configuration
public class MiscConfig extends WebMvcConfigurerAdapter {

  /**
   * CORS configuration.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**"). //
        allowedOrigins("*"). //
        allowedMethods("POST"). //
        allowedHeaders("Content-Type", "Authorization"). //
        allowCredentials(true). //
        maxAge(3600);
  }

  /**
   * Configuration to support lazy loading on Jackson side. Custom-configured Jackson message
   * converter. Preventing exception: JsonMappingException: could not initialize proxy - no Session.
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jacksonMessageConverter());
    super.configureMessageConverters(converters);
  }

  private MappingJackson2HttpMessageConverter jacksonMessageConverter() {

    // Configuring Jackson message converter with {@link Hibernate5Module}.
    MappingJackson2HttpMessageConverter messageConverter = //
        new MappingJackson2HttpMessageConverter();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Hibernate5Module());

    messageConverter.setObjectMapper(mapper);
    return messageConverter;

  }
}