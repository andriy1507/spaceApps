package com.spaceapps.myapplication.utils

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree

@ExperimentalComposeUiApi
fun Modifier.autofill(
    onFill: ((String) -> Unit),
    vararg autofillTypes: AutofillType
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode =
        remember { AutofillNode(onFill = onFill, autofillTypes = autofillTypes.toList()) }
    LocalAutofillTree.current += autofillNode
    onGloballyPositioned { autofillNode.boundingBox = it.boundsInWindow() }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}
