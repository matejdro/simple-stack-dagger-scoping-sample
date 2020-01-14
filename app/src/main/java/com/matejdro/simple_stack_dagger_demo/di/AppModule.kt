package com.matejdro.simple_stack_dagger_demo.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}