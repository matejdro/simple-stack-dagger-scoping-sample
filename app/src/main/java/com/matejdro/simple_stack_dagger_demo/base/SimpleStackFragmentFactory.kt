package com.matejdro.simple_stack_dagger_demo.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Reusable
import javax.inject.Inject
import javax.inject.Provider

@Reusable
class SimpleStackFragmentFactory @Inject constructor(
    private val factories: Map<
        @JvmSuppressWildcards Class<*>,
        @JvmSuppressWildcards Provider<Fragment>
        >
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)

        val fragmentProvider = factories[fragmentClass]

        return if (fragmentProvider != null) {
            fragmentProvider.get()
        } else {
            try {
                super.instantiate(classLoader, className)
            } catch (e: Fragment.InstantiationException) {
                throw IllegalStateException(
                    "Fragment $className cannot be created. Did you add it to the Module?",
                    e
                )
            }
        }
    }
}