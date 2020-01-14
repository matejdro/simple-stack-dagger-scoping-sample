/*
 * Copyright © 2016, Connected Travel, LLC – All Rights Reserved.
 *
 * All information contained herein is property of Connected Travel, LLC including, but
 * not limited to, technical and intellectual concepts which may be embodied within.
 *
 * Dissemination or reproduction of this material is strictly forbidden unless prior written
 * permission, via license, is obtained from Connected Travel, LLC. If permission is obtained,
 * this notice, and any other such legal notices, must remain unaltered.
 */

package com.matejdro.simple_stack_dagger_demo.base

import com.matejdro.simple_stack_dagger_demo.di.ScopedServiceBinding
import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.ServiceBinder
import javax.inject.Inject
import javax.inject.Provider

class DaggerScopedServices @Inject constructor(
    @ScopedServiceBinding
    private val serviceFactories: Map<
        @JvmSuppressWildcards Class<*>,
        @JvmSuppressWildcards Provider<Any>
        >
) : ScopedServices {
    override fun bindServices(serviceBinder: ServiceBinder) {
        val cls = try {
            Class.forName(serviceBinder.scopeTag)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("Scoped service ${serviceBinder.scopeTag} not found.", e)
        }

        val factory = serviceFactories[cls]
            ?: error("Service ${serviceBinder.scopeTag} not injected")

        serviceBinder.addService(serviceBinder.scopeTag, factory.get())
    }
}