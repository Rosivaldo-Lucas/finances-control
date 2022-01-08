package com.rasmoo.client.financescontroll.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class OAuthConfiguration {

  private static final String RESOURCE_ID = "finances-controll";

  @EnableAuthorizationServer
  public static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationServer(final AuthenticationManager authenticationManager, final PasswordEncoder passwordEncoder) {
      this.authenticationManager = authenticationManager;
      this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
      clients
              .inMemory()
              .withClient("cliente-web")
              .secret(this.passwordEncoder.encode("web"))
              .authorizedGrantTypes("password")
              .scopes("read", "write")
              .accessTokenValiditySeconds(3601)
              .resourceIds(RESOURCE_ID)
              .and()
              .withClient("cliente-canva")
              .secret(this.passwordEncoder.encode("canva"))
              .authorizedGrantTypes("authorization_code")
              .redirectUris("https://github.com/Rosivaldo-Lucas/finances-control")
              .scopes("read")
              .accessTokenValiditySeconds(3601)
              .resourceIds(RESOURCE_ID);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints.authenticationManager(this.authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
      super.configure(security);
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
