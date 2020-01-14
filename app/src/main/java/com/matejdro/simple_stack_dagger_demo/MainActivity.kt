package com.matejdro.simple_stack_dagger_demo

import android.os.Bundle
import com.matejdro.simple_stack_dagger_demo.base.FragmentStateChanger
import com.matejdro.simple_stack_dagger_demo.di.BackstackModule
import com.matejdro.simple_stack_dagger_demo.di.MainActivitySubcomponent
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.navigator.BackstackActivity
import com.zhuinden.simplestack.navigator.Navigator
import com.matejdro.simple_stack_dagger_demo.features.words.WordListKey

class MainActivity : BackstackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val componentFactory: MainActivitySubcomponent.Factory =
            (application as DemoApplication).mainActivitySubcomponentFactory

        val backstackModule = BackstackModule()
        val component = componentFactory.create(backstackModule)

        supportFragmentManager.fragmentFactory = component.provideFragmentFactory()

        val backstackInstaller = Navigator.configure()
            .setScopedServices(component.provideScopedServices())

        val backstack =
            initBackstack(backstackInstaller, savedInstanceState, listOf(WordListKey))
        backstackModule.backstack = backstack

        if (savedInstanceState != null) {
            backstack.initStateChanger()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            backstack.initStateChanger()
        }
    }

    /**
     * HACK:
     *
     * setStateChanger() line appears to both initialize scoped services and
     * trigger first state change.
     *
     * When activity is created for the first time, we want to set state changer AFTER
     * super.onCreate() when fragment manager is ready, so simple-stack can perform first state change.
     *
     * However, when activity is restored from saved state, we need scoped services to be initialized
     * when fragments are being recreated (so we can perform constructor injection).
     * That is why state changer needs to set BEFORE super.onCreate(). First state change does
     * not appear to be the problem, since there no state changes need to be performed - everything
     * is restored from saved state.
     */
    private fun Backstack.initStateChanger() {
        setStateChanger(
            FragmentStateChanger(
                supportFragmentManager.fragmentFactory,
                supportFragmentManager,
                R.id.container
            )
        )
    }
}
