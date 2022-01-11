package com.rasmoo.client.financescontroll.oauth;

import com.rasmoo.client.financescontroll.v1.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

  private static final String RESOURCE_ID = "finances-controll";

  @Autowired
  private UserInfoService userInfoService;

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) {
    resources.resourceId(RESOURCE_ID);
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .anyRequest()
            .authenticated();
  }

  @Bean
  public UserAuthenticationConverter userTokenConverter() {
    final DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
    converter.setUserDetailsService(this.userInfoService);

    return converter;
  }

  @Bean
  public AccessTokenConverter defaultAccesTokenConverter() {
    final DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    tokenConverter.setUserTokenConverter(this.userTokenConverter());

    return tokenConverter;
  }


  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
    accessTokenConverter.setAccessTokenConverter(this.defaultAccesTokenConverter());
    accessTokenConverter.setSigningKey("teste");

    return accessTokenConverter;
  }

  @Bean
  public DefaultTokenServices defaultTokenServices() {
    final DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(this.tokenStore());

    return tokenServices;
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(this.accessTokenConverter());
  }

}
