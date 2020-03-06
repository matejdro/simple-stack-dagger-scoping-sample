package com.matejdro.simple_stack_dagger_demo.features.words

import androidx.fragment.app.Fragment
import com.matejdro.simple_stack_dagger_demo.di.ScopedService
import com.matejdro.simple_stack_dagger_demo.di.ScopedServiceBinding
import com.zhuinden.simplestack.Backstack
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class WordsModule {

    // Fragment bindings

    @Binds
    @IntoMap
    @ClassKey(NewWordFragment::class)
    abstract fun bindNewWordFragment(newWordFragment: NewWordFragment): Fragment

    @Binds
    @IntoMap
    @ClassKey(WordListFragment::class)
    abstract fun bindWordListFragment(wordListFragment: WordListFragment): Fragment

    // Service bindings

    @Binds
    @IntoMap
    @ScopedServiceBinding
    @ClassKey(WordController::class)
    abstract fun bindWordController(wordController: WordController): Any

    @Module
    companion object {
        // More Aliases

        @JvmStatic
        @Provides
        @ScopedService
        fun provideEventEmitter(
            @ScopedService wordController: WordController
        ) = wordController.eventEmitter

        // Providers to inject services from Backstack

        @JvmStatic
        @Provides
        @ScopedService
        fun provideLoginViewModel(
            backstack: Backstack
        ): WordController {
            return backstack.lookupService(WordController::class.java.name)
        }
    }
}