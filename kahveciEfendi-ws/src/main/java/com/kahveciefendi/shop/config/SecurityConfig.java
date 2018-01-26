package com.kahveciefendi.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * Spring security configuration which configures simple authorization and authentication in-memory
 * for demo purposes.
 * 
 * @author Ayhan Dardagan
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String REALM = "KahveciEfendi";

  /**
   * In-memory access (authorization and authentication) settings.
   * 
   * @param authManagerBuilder
   *          Authentication manager builder
   * @throws Exception
   *           Exception
   */
  @Autowired
  public void configureGlobalSecurity(AuthenticationManagerBuilder authManagerBuilder)
      throws Exception {
    authManagerBuilder.inMemoryAuthentication().withUser("hakan").password("hakan").roles("ADMIN");
    authManagerBuilder.inMemoryAuthentication().withUser("john").password("john").roles("USER");
    authManagerBuilder.inMemoryAuthentication().withUser("jessica").password("jessica")
        .roles("USER");
  }

  /**
   * Basic authentication entry point settings.
   * 
   * @return Basic authentication entry point
   */
  @Bean
  public BasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName(REALM);
    return entryPoint;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable().authorizeRequests()
        .antMatchers("/drinks", "/drinkadditions", "/calculatediscount").permitAll()
        .antMatchers("/giveorder").access("hasRole('USER') or hasRole('ADMIN')")
        .antMatchers("/userreport", "/drinkreport", "/drinkadditionreport", "/ordereddrinknames",
            "/ordereddrinkadditionnames", "/saveupdatedrink", "/saveupdatedrinkaddition",
            "/deletedrink", "/deletedrinkaddition")
        .access("hasRole('ADMIN')").and().httpBasic().realmName(REALM)
        .authenticationEntryPoint(getBasicAuthEntryPoint());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  }
}
