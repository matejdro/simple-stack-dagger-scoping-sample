package com.matejdro.simple_stack_dagger_demo

import android.app.Application
import com.matejdro.simple_stack_dagger_demo.di.DaggerAppComponent
import com.matejdro.simple_stack_dagger_demo.di.MainActivitySubcomponent

class DemoApplication : Application() {
    lateinit var mainActivitySubcomponentFactory: MainActivitySubcomponent.Factory
        private set

    override fun onCreate() {
        val component = DaggerAppComponent.factory().create(this)

        mainActivitySubcomponentFactory = component.provideMainActivitySubcomponentFactory()

        super.onCreate()
    }
}