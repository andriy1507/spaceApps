package com.spaceapps.myapplication.features.createFeed

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun CreateFeedScreen(vm: CreateFeedViewModel) = Column {

    val title by vm.title.observeAsState()
    val text by vm.text.observeAsState()

    TextField(value = title.orEmpty(), onValueChange = vm::onTitleEnter)
    TextField(value = text.orEmpty(), onValueChange = vm::onTextEnter)
    Button(onClick = vm::createFeed) {
        Text(text = "Create")
    }

}
