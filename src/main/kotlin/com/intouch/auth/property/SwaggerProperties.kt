/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "intouch.microbase.swagger")
class SwaggerProperties {
    var basePackage = ""
    var apiVersion = ""
    var title = ""
    var description = ""
}