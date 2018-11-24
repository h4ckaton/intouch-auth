/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth

import javax.persistence.EntityManager

inline fun <reified T, X> EntityManager.findBy(attributeName: String, value: X): T =
        createQuery(criteriaBuilder.createQuery(T::class.java).let {
            val root = it.from(T::class.java)
            it.select(root).where(criteriaBuilder.equal(root.get<X>(attributeName), value))
        }).resultList.first()

inline fun <reified T, X> EntityManager.findAllBy(attributeName: String, value: X): MutableList<T> =
        createQuery(criteriaBuilder.createQuery(T::class.java).let {
            val root = it.from(T::class.java)
            it.select(root).where(criteriaBuilder.equal(root.get<X>(attributeName), value))
        }).resultList

inline fun <reified T, X> EntityManager.remove(attributeName: String, value: X) =
    createQuery(criteriaBuilder.createCriteriaDelete(T::class.java).apply {
        where(criteriaBuilder.equal(from(T::class.java).get<X>(attributeName), value))
    }).executeUpdate()