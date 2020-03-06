package com.matejdro.simple_stack_dagger_demo.features.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.matejdro.simple_stack_dagger_demo.R
import com.matejdro.simple_stack_dagger_demo.di.ScopedService
import com.matejdro.simple_stack_dagger_demo.utils.utils.observe
import com.matejdro.simple_stack_dagger_demo.utils.utils.onClick
import com.matejdro.simple_stack_dagger_demo.utils.utils.safe
import com.matejdro.simple_stack_dagger_demo.utils.utils.showToast
import kotlinx.android.synthetic.main.word_list_view.*
import javax.inject.Inject

class WordListFragment @Inject constructor(
    @ScopedService private val wordController: WordController,
    @ScopedService private val eventEmitter: WordEventEmitter
) : Fragment() {
    val adapter = WordListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.word_list_view, container, false)

    @Suppress("NAME_SHADOWING")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        buttonGoToAddNewWord.onClick { view ->
            wordController.onAddNewWordClicked(this)
        }

        wordController.wordList.observe(this /*getViewLifecycleOwner()*/, Observer { words ->
            adapter.updateWords(words!!)
        })

        eventEmitter.observe(this /*getViewLifecycleOwner()*/) { event ->
            when (event) {
                is WordController.Events.NewWordAdded -> showToast("Added ${event.word}")
            }.safe()
        }
    }
}