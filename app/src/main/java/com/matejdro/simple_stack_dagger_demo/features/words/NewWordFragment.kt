package com.matejdro.simple_stack_dagger_demo.features.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.matejdro.simple_stack_dagger_demo.R
import com.matejdro.simple_stack_dagger_demo.di.ScopedService
import kotlinx.android.synthetic.main.new_word_fragment.*
import javax.inject.Inject

class NewWordFragment @Inject constructor(
    @ScopedService private val actionHandler: ActionHandler
) : Fragment() {
    interface ActionHandler {
        fun onAddWordClicked(newWordFragment: NewWordFragment, word: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.new_word_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddNewWord.setOnClickListener {
            val word = textInputNewWord.text.toString().trim()
            actionHandler.onAddWordClicked(this, word)
        }
    }
}
