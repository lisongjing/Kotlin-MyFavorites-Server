package com.orchidpine.myfavorites.favorite.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.RemoteTokenServices
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

@EnableResourceServer
@Configuration
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
                .antMatchers("/actuator/**")
                .permitAll()
                .anyRequest()
                .authenticated()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources!!.tokenServices(tokenServices())
    }

    @Value("\${security.oauth2.resource.token-info-uri}")
    private val checkTokenUrl: String? = null

    @Value("\${security.oauth2.client.client-id}")
    private val clientId: String? = null

    @Value("\${security.oauth2.client.client-secret}")
    private val clientSecret: String? = null

    @Bean
    fun tokenServices(): ResourceServerTokenServices {
        val remoteTokenServices = RemoteTokenServices()
        remoteTokenServices.setCheckTokenEndpointUrl(checkTokenUrl)
        remoteTokenServices.setClientId(clientId)
        remoteTokenServices.setClientSecret(clientSecret)
        return remoteTokenServices
    }
}