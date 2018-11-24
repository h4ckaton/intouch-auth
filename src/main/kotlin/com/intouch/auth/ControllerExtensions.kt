/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun putResponse(success: Boolean) = ResponseEntity<Unit>(if (success) HttpStatus.RESET_CONTENT else HttpStatus.BAD_REQUEST)