// HACK: This class resides in simple-stack package to allow access to navigator installer fields.
package com.matejdro.simple_stack_dagger_demo

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
        savedInstanceState: Bundle?,
        initialKeys: List<*>,
        backstackConfiguration: Backstack.() -> Unit): Backstack {
        val factory =
            BackstackHolderViewModelFactory(
                backstackConfiguration,
                initialKeys
            )

        val viewModel = ViewModelProviders.of(this, factory).get<BackstackHolderViewModel>()
        backstack = viewModel.backstack

        if (savedInstanceState != null) {
            backstack.fromBundle(savedInstanceState.getParcelable("BACKSTACK_STATE"))
        }

        return backstack
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("BACKSTACK_STATE", backstack.toBundle())
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
        private val backstackConfiguration: Backstack.() -> Unit,
        private val initialKeys: List<*>
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val backstack = Backstack()
            backstackConfiguration.invoke(backstack)
            backstack.setup(initialKeys)

            @Suppress("UNCHECKED_CAST")
            return BackstackHolderViewModel(
                backstack
            ) as T
        }
    }
}