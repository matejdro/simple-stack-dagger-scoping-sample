package com.matejdro.simple_stack_dagger_demo.utils

import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger

object NoOpStateChanger : StateChanger {
    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {
        completionCallback.stateChangeComplete()
    }
}