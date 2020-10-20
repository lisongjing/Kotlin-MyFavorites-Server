package com.orchidpine.myfavorites.account.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

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
}