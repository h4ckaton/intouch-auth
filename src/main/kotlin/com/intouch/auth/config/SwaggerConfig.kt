/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.config

import com.intouch.auth.property.SwaggerProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties::class)
@ComponentScan(basePackageClasses = [SwaggerProperties::class])
class SwaggerConfig @Autowired constructor(private val swaggerProperties: SwaggerProperties) {
    @Bean
    fun api() = Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getSwaggerInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage(swaggerProperties.basePackage))
            .paths(PathSelectors.any())
            .build()
            .useDefaultResponseMessages(false)
            .ignoredParameterTypes(User::class.java)
            .ignoredParameterTypes(Authentication::class.java)
            .enable(true)

    private fun getSwaggerInfo() = ApiInfoBuilder()
            .title(swaggerProperties.title)
            .description(swaggerProperties.description)
            .version(swaggerProperties.apiVersion)
            .build()
}