package com.matejdro.simple_stack_dagger_demo.di

import androidx.fragment.app.FragmentFactory
import com.matejdro.simple_stack_dagger_demo.features.words.WordsModule
import com.zhuinden.simplestack.ScopedServices
import dagger.Subcomponent

@Subcomponent(
    modules = [
        BackstackModule::class,
        MainActivityModule::class,
        WordsModule::class
    ]
)
interface MainActivitySubcomponent {
    fun provideFragmentFactory(): FragmentFactory
    fun provideScopedServices(): ScopedServices

    @Subcomponent.Factory
    interface Factory {
        fun create(backstack: BackstackModule): MainActivitySubcomponent
    }
}