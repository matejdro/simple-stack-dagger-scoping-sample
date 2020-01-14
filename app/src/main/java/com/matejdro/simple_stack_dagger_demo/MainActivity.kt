package com.matejdro.simple_stack_dagger_demo

import android.os.Bundle
import com.matejdro.simple_stack_dagger_demo.base.FragmentStateChanger
import com.matejdro.simple_stack_dagger_demo.di.BackstackModule
import com.matejdro.simple_stack_dagger_demo.di.MainActivitySubcomponent
import com.matejdro.simple_stack_dagger_demo.features.words.WordListKey
import com.matejdro.simple_stack_dagger_demo.utils.NoOpStateChanger

class MainActivity : BackstackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val componentFactory: MainActivitySubcomponent.Factory =
            (application as DemoApplication).mainActivitySubcomponentFactory

        val backstackModule = BackstackModule()
        val component = componentFactory.create(backstackModule)

        supportFragmentManager.fragmentFactory = component.provideFragmentFactory()

        val backstack =
            initBackstack(savedInstanceState, listOf(WordListKey)) {
                setScopedServices(component.provideScopedServices())
            }

        backstackModule.backstack = backstack

        backstack.setStateChanger(NoOpStateChanger) // force creation of scoped services
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backstack.setStateChanger(
            FragmentStateChanger(
                supportFragmentManager.fragmentFactory,
                supportFragmentManager,
                R.id.container
            )
        )
    }
}
