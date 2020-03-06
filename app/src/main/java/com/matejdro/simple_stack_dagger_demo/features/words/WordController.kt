package com.matejdro.simple_stack_dagger_demo.features.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import javax.inject.Inject

class WordEventEmitter(
    private val eventEmitter: EventSource<WordController.Events>
) : EventSource<WordController.Events> by eventEmitter

class WordController @Inject constructor(
    private val backstack: Backstack
) : Bundleable {
    sealed class Events {
        data class NewWordAdded(val word: String) : Events()
    }

    private val wordEventEmitter = EventEmitter<Events>()
    val eventEmitter = WordEventEmitter(wordEventEmitter)

    private val mutableWords: MutableLiveData<List<String>> = MutableLiveData()
    val wordList: LiveData<List<String>>
        get() = mutableWords

    init {
        mutableWords.value = listOf("Bogus", "Magic", "Scoping mechanisms")
    }

    private fun addWordToList(word: String) {
        mutableWords.run {
            postValue(value!!.toMutableList().also { list -> list.add(word) })
        }
        wordEventEmitter.emit(Events.NewWordAdded(word))
    }

    fun onAddNewWordClicked(wordListFragment: WordListFragment) {
        backstack.goTo(NewWordKey)
    }

    fun onAddWordClicked(newWordFragment: NewWordFragment, word: String) {
        if (word.isNotEmpty()) {
            addWordToList(word)
        }
        backstack.goBack()
    }

    // NOTE: Data is typically in the database, so do this only for non-transient state.
    override fun toBundle(): StateBundle =
        StateBundle().putStringArrayList("words", mutableWords.value?.let { ArrayList(it) })

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            mutableWords.value = getStringArrayList("words")
        }
    }
}