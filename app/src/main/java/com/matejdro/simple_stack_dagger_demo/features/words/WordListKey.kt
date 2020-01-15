package com.matejdro.simple_stack_dagger_demo.features.words

import com.matejdro.simple_stack_dagger_demo.base.FragmentKey
import com.zhuinden.simplestack.ScopeKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Zhuinden on 2018.09.17.
 */
@Parcelize
object WordListKey : FragmentKey(), ScopeKey.Child {
    override val fragmentClassName: String
        get() = WordListFragment::class.java.name

    override fun getParentScopes(): List<String> =
        listOf(WordController::class.java.name)

    override val fragmentTag: String
        get() = javaClass.name // object toString() includes hashCode()
}
