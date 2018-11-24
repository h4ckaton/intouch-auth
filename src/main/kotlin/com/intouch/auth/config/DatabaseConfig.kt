/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.config

import com.intouch.auth.property.DatabaseProperties
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import java.io.PrintWriter
import javax.sql.DataSource


@Configuration
@ComponentScan(basePackageClasses = [DatabaseProperties::class])
@EnableConfigurationProperties(DatabaseProperties::class)
class DatabaseConfig @Autowired constructor(private val databaseProperties: DatabaseProperties) {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource() = HikariDataSource().apply {
        logWriter = (PrintWriter(System.out))
        isAutoCommit = true
        maxLifetime = 300000 // 5 minutes
        driverClassName = "org.postgresql.Driver"
    }

    @Bean
    @Primary
    fun entityManagerFactory(dataSource: DataSource, jpaVendorAdapter: JpaVendorAdapter) = LocalContainerEntityManagerFactoryBean().apply {
        setDataSource(dataSource())
        setJpaVendorAdapter(jpaVendorAdapter)
        setPackagesToScan(databaseProperties.packagesToScan)
    }

    companion object {
        const val SCHEMA = "public"
    }
}