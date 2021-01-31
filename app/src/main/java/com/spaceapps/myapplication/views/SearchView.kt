package com.spaceapps.myapplication.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.MyApplicationTheme

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChanged: (String) -> Unit,
    onSearchPerformed: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        value = query,
        onValueChange = onQueryChanged,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colors.primary
            )
        },
        trailingIcon = {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_search),
                tint = MaterialTheme.colors.primary
            )
        },
        onImeActionPerformed = { action, controller ->
            if (action == ImeAction.Search) {
                onSearchPerformed()
                controller?.hideSoftwareKeyboard()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyApplicationTheme {
        Surface {
            SearchView(
                onQueryChanged = {},
                onSearchPerformed = {}
            )
        }
    }
}
