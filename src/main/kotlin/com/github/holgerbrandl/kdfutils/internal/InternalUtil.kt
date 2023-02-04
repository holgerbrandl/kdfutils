package com.github.holgerbrandl.kdfutils.internal

import kotlin.reflect.KCallable
import kotlin.reflect.KProperty
import kotlin.reflect.full.starProjectedType


//todo move to internal namespace to prevent API clutter
inline fun <reified T> detectPropertiesByReflection(): List<KCallable<*>> {
    val members = T::class.members

    val propsOrGetters = members.filter {
        //        it.parameters.isEmpty() // -> wrong because self pointer needs to be provided
        when (it) {
            is KProperty -> true
            else -> {
                val starProjectedType = T::class.starProjectedType
                it.parameters.size == 1 && it.parameters.first().type == starProjectedType
            }
        }
    }

    return propsOrGetters.filterNot { it.name.run { equals("toString") || equals("hashCode") || matches("component[1-9][0-9]*".toRegex()) } }
}