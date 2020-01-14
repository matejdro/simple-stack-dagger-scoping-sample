// HACK: This class resides in simple-stack package to allow access to navigator installer fields.
package com.zhuinden.simplestack.navigator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.zhuinden.simplestack.Backstack

/**
 * Base activity that uses ViewModel to manage the lifecycle of simple-stack's [Backstack].
 *
 * Since we want our backstack to be ready before fragments are initialized
 * (so we can use constructor injection), we have to use this instead of [Navigator].
 *
 * To use this stack, you MUST call [initBackstack] manually.
 */
abstract class BackstackActivity : AppCompatActivity() {
    private lateinit var backstack: Backstack

    /**
     * Init the backstack instance.
     *
     * This method MUST be called in Activity.onCreate, but it can be called before
     * super.onCreate (before fragments are re-created).
     *
     * Created backstack will not have state changer installed. You need to install it yourself.
     */
    protected fun initBackstack(
        installer: Navigator.Installer,
        savedInstanceState: Bundle?,
        initialKeys: List<*>
    ): Backstack {
        val factory = BackstackHolderViewModelFactory(
            installer,
            initialKeys
        )

        val viewModel = ViewModelProviders.of(this, factory).get<BackstackHolderViewModel>()
        backstack = viewModel.backstack

        if (savedInstanceState != null) {
            backstack.fromBundle(savedInstanceState.getParcelable("NAVIGATOR_STATE_BUNDLE"))
        }

        return backstack
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("NAVIGATOR_STATE_BUNDLE", backstack.toBundle())
    }

    override fun onResume() {
        super.onResume()
        backstack.reattachStateChanger()
    }

    override fun onPause() {
        backstack.detachStateChanger()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        backstack.executePendingStateChange()
        backstack.finalizeScopes()
    }

    private class BackstackHolderViewModel(val backstack: Backstack) : ViewModel()

    private class BackstackHolderViewModelFactory(
        private val installer: Navigator.Installer,
        private val initialKeys: List<*>
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val backstack = Backstack()
            backstack.setKeyFilter(installer.keyFilter)
            backstack.setKeyParceler(installer.keyParceler)
            backstack.setStateClearStrategy(installer.stateClearStrategy)
            if (installer.scopedServices != null) {
                backstack.setScopedServices(installer.scopedServices)
            }
            if (installer.globalServices != null) {
                backstack.setGlobalServices(installer.globalServices)
            }
            if (installer.globalServiceFactory != null) {
                backstack.setGlobalServices(installer.globalServiceFactory)
            }
            backstack.setup(initialKeys)
            for (completionListener in installer.stateChangeCompletionListeners) {
                backstack.addStateChangeCompletionListener(completionListener)
            }

            @Suppress("UNCHECKED_CAST")
            return BackstackHolderViewModel(backstack) as T
        }
    }
}