package com.rasmoo.client.financescontroll.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class OAuthConfiguration {

  private static final String RESOURCE_ID = "finances-controll";

  @EnableAuthorizationServer
  public static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
      clients
              .inMemory()
              .withClient("cliente-web")
              .secret("web")
              .resourceIds(RESOURCE_ID);
    }

  }

  @EnableResourceServer
  public static class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
      resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
      http
              .authorizeRequests()
              .anyRequest()
              .authenticated()
              .and()
              .requestMatchers()
              .antMatchers("/v2/categorias");
    }

  }

}
