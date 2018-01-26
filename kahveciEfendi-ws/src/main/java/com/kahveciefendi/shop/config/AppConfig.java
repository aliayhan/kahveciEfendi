package com.kahveciefendi.shop.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Spring app configuration which configures project package to scan for Spring Configurations,
 * Services, etc.
 * 
 * @author Ayhan Dardagan
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.kahveciefendi.shop")
public class AppConfig {

  /**
   * Message source which supports reloading.
   * 
   * @return Message source
   */
  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = //
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}