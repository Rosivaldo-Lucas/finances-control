package com.rasmoo.client.financescontroll.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

  private static final String RESOURCE_ID = "finances-controll";

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients
            .inMemory()
            .withClient("cliente-web")
            .secret(this.passwordEncoder.encode("web"))
            .authorizedGrantTypes("password", "refresh_token")
            .scopes("read", "write", "logado", "nao_logado")
            .accessTokenValiditySeconds(3600) // 1 hora
            .refreshTokenValiditySeconds(86400) // 24 horas
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
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
            .authenticationManager(this.authenticationManager)
            .userDetailsService(this.userDetailsService)
            .tokenStore(this.jwtTokenStore())
            .accessTokenConverter(this.jwtAccessTokenConverter())
            .reuseRefreshTokens(Boolean.FALSE);
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    final JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
    accessTokenConverter.setSigningKey("teste");

    return accessTokenConverter;
  }

  @Bean
  public TokenStore jwtTokenStore() {
    return new JwtTokenStore(this.jwtAccessTokenConverter());
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(this.jwtTokenStore());
    defaultTokenServices.setSupportRefreshToken(Boolean.TRUE);

    return defaultTokenServices;
  }

}
