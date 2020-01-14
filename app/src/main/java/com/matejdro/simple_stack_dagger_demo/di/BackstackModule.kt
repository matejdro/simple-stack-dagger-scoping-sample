package com.matejdro.simple_stack_dagger_demo.di

import com.zhuinden.simplestack.Backstack
import dagger.Module
import dagger.Provides

@Module
class BackstackModule {
    @get:Provides
    lateinit var backstack: Backstack
}