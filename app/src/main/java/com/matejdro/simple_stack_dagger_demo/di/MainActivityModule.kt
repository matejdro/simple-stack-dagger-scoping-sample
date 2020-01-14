package com.matejdro.simple_stack_dagger_demo.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.matejdro.simple_stack_dagger_demo.base.DaggerScopedServices
import com.matejdro.simple_stack_dagger_demo.base.SimpleStackFragmentFactory
import com.zhuinden.simplestack.ScopedServices
import dagger.Binds
import dagger.Module
import dagger.multibindings.Multibinds

@Module
abstract class MainActivityModule {
    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: SimpleStackFragmentFactory): FragmentFactory

    @Multibinds
    abstract fun fragmentsMultibinds(): Map<Class<*>, Fragment>

    @Binds
    abstract fun bindScopedServices(daggerScopedServices: DaggerScopedServices): ScopedServices

    @Multibinds
    @ScopedServiceBinding
    abstract fun bindScopedServiceBindings(): Map<String, Any>

}