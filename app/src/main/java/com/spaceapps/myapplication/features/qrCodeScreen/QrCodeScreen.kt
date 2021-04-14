package com.spaceapps.myapplication.features.qrCodeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_48

@Composable
fun QrCodeScreen(vm: QrCodeViewModel) =
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val bitmap by vm.image.observeAsState()
        val input by vm.userInput.observeAsState()
        if (bitmap != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = ""
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = SPACING_16.dp)
                .padding(top = SPACING_48.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            value = input.orEmpty(),
            onValueChange = vm::onUserInput,
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { vm.onUserInput("") },
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                )
            }
        )
    }
