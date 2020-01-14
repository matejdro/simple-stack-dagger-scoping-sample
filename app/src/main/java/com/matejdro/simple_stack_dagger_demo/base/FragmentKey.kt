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

import android.os.Parcelable
import androidx.fragment.app.Fragment

abstract class FragmentKey() : Parcelable {
    abstract val fragmentClassName: String

    open val fragmentTag: String
        get() = toString()

    companion object {
        const val ARGUMENT_TAG = "KEY"
    }
}

fun <T : FragmentKey> Fragment.getKey(): T {
    return arguments?.getParcelable(FragmentKey.ARGUMENT_TAG)
        ?: error("Key is not present. Was this fragment started via simple-stack?")
}
