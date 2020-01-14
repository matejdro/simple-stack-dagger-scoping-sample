/*
 * Copyright © 2016, Connected Travel, LLC – All Rights Reserved.
 *
 * All information contained herein is property of Connected Travel, LLC including, but
 * not limited to, technical and intellectual concepts which may be embodied within.
 *
 * Dissemination or reproduction of this material is strictly forbidden unless prior written
 * permission, via license, is obtained from Connected Travel, LLC. If permission is obtained,
 * this notice, and any other such legal notices, must remain unaltered.
 */

package com.matejdro.simple_stack_dagger_demo.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger

/**
 * Based on
 *
 * https://github.com/Zhuinden/simple-stack/blob/6c7d4f8d9ba921980423972738b03b53c0b3d9ba/
 * simple-stack-example-mvp-fragments/src/main/java/com/zhuinden/simplestackdemoexamplefragments/
 * util/FragmentStateChanger.kt
 */
class FragmentStateChanger(
    private val fragmentFactory: FragmentFactory,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : StateChanger {
    override fun handleStateChange(
        stateChange: StateChange,
        completionCallback: StateChanger.Callback
    ) {
        if (stateChange.isTopNewKeyEqualToPrevious) {
            completionCallback.stateChangeComplete()
            return
        }

        val fragmentTransaction = fragmentManager.beginTransaction().disallowAddToBackStack()
        when (stateChange.direction) {
            StateChange.FORWARD -> {
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
            StateChange.BACKWARD -> {
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            }
        }

        val previousState = stateChange.getPreviousKeys<FragmentKey>()
        val newState = stateChange.getNewKeys<FragmentKey>()

        for (oldKey in previousState) {
            removeOldFragment(oldKey, newState, fragmentTransaction, stateChange)
        }

        for (newKey in newState) {
            addNewFragments(newKey, stateChange, fragmentTransaction)
        }

        fragmentTransaction.commitNowAllowingStateLoss()
        completionCallback.stateChangeComplete()
    }

    private fun removeOldFragment(
        oldKey: FragmentKey,
        newState: History<FragmentKey>,
        fragmentTransaction: FragmentTransaction,
        stateChange: StateChange
    ) {
        val fragment = fragmentManager.findFragmentByTag(oldKey.fragmentTag)
        if (fragment != null) {
            if (!newState.contains(oldKey)) {
                fragmentTransaction.remove(fragment)
            } else if (!fragment.isDetached && oldKey != stateChange.topNewKey<Any>()) {
                fragmentTransaction
                    .detach(fragment)
            }
        }
    }

    private fun addNewFragments(
        newKey: FragmentKey,
        stateChange: StateChange,
        fragmentTransaction: FragmentTransaction
    ) {
        val fragment: Fragment? = fragmentManager.findFragmentByTag(newKey.fragmentTag)
        if (newKey == stateChange.topNewKey<Any>()) {
            if (fragment != null && !fragment.isRemoving) {
                if (fragment.isDetached) {

                    fragmentTransaction
                        .attach(fragment)
                }
            } else {
                val newFragment = fragmentFactory.instantiate(
                    javaClass.classLoader!!,
                    newKey.fragmentClassName
                )

                newFragment.arguments = Bundle().apply {
                    putParcelable(FragmentKey.ARGUMENT_TAG, newKey)
                }

                fragmentTransaction.add(
                    containerId,
                    newFragment,
                    newKey.fragmentTag
                )
            }
        } else {
            if (fragment != null && !fragment.isDetached) {
                fragmentTransaction
                    .detach(fragment)
            }
        }
    }
}