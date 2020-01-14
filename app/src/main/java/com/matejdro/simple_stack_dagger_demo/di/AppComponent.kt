package com.matejdro.simple_stack_dagger_demo.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubcomponentsModule::class
    ]
)
interface AppComponent {
    fun provideMainActivitySubcomponentFactory(): MainActivitySubcomponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}