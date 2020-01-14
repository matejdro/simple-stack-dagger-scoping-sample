package com.matejdro.simple_stack_dagger_demo.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainActivitySubcomponent::class])
abstract class SubcomponentsModule {
    @Binds
    @IntoMap
    @ClassKey(MainActivitySubcomponent.Factory::class)
    abstract fun bindMobileActivitySubcomponentFactory(
        factory: MainActivitySubcomponent.Factory
    ): Any
}