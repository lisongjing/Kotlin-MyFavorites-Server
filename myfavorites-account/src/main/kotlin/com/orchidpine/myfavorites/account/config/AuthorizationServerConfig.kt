package com.orchidpine.myfavorites.account.config

import com.orchidpine.myfavorites.account.model.environment.CustomParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore

@EnableAuthorizationServer
@Configuration
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {
    @Autowired
    private lateinit var redisConnectionFactory: RedisConnectionFactory

    @Autowired
    private lateinit var params: CustomParams

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder{
        return BCryptPasswordEncoder()
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.tokenKeyAccess("permitAll()") // /oauth/token_key
                .checkTokenAccess("isAuthenticated()") // /oauth/check_token
                .passwordEncoder(passwordEncoder())
                .allowFormAuthenticationForClients()
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                // Web client
                .withClient(params.webClientId)
                .secret(params.webClientSecret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes(params.webClientScopes)
                .accessTokenValiditySeconds(params.webAccessValiditySeconds)
                .refreshTokenValiditySeconds(params.webRefreshValiditySeconds)
                // Mobile client
                .and()
                .withClient(params.mobileClientId)
                .secret(params.mobileClientSecret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes(params.mobileClientScopes)
                .accessTokenValiditySeconds(params.mobileAccessValiditySeconds)
                .refreshTokenValiditySeconds(params.mobileRefreshValiditySeconds)
                // Visitor client
                .and()
                .withClient(params.visitorClientId)
                .secret(params.visitorClientSecret)
                .authorizedGrantTypes("client_credentials")
                .scopes(params.visitorClientScopes)
                .accessTokenValiditySeconds(params.visitorAccessValiditySeconds)
                .refreshTokenValiditySeconds(params.visitorRefreshValiditySeconds)
                .and()
                // Internal server
                .withClient(params.serverInternalId)
                .secret(params.serverInternalSecret)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.authenticationManager(authenticationManager)
                .tokenStore(RedisTokenStore(redisConnectionFactory))
    }
}