/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth

import arrow.core.Option
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

inline fun <reified T> ModelMapper.postResponse(source: Option<Any>) =
        source.fold({
            ResponseEntity<T>(HttpStatus.CONFLICT)
        }, {
            ResponseEntity(map(it, T::class.java), HttpStatus.CREATED)
        })

inline fun <reified T> ModelMapper.getResponse(source: Option<Any>) =
        source.fold({
            ResponseEntity<T>(HttpStatus.NOT_FOUND)
        }, {
            ResponseEntity(map(it, T::class.java), HttpStatus.OK)
        })

inline fun <reified T> ModelMapper.putResponse(source: Option<Any>) =
        source.fold({
            ResponseEntity<T>(HttpStatus.BAD_REQUEST)
        }, {
            ResponseEntity(map(it, T::class.java), HttpStatus.NO_CONTENT)
        })

inline fun <reified T> ModelMapper.deleteResponse(source: Option<Any>) =
        source.fold({
            ResponseEntity<T>(HttpStatus.BAD_REQUEST)
        }, {
            ResponseEntity(map(it, T::class.java), HttpStatus.NO_CONTENT)
        })