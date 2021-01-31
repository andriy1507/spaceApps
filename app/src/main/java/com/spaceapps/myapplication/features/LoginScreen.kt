package com.spaceapps.myapplication.features

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.ui.MyApplicationTheme
import com.spaceapps.myapplication.ui.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Suppress("LongMethod")
fun LoginScreen() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val vertCenter = createGuidelineFromStart(.5f)
        val (
            loginField,
            passwordField,
            confirmField,
            mainButton,
            termsLabel,
            policyLabel,
            stateButton,
            dot
        ) = createRefs()
        OutlinedTextField(
            modifier = Modifier.constrainAs(loginField) {
                bottom.linkTo(passwordField.top, SPACING_16.dp)
            }.fillMaxWidth().padding(horizontal = SPACING_24.dp),
            value = "",
            onValueChange = {}
        )
        OutlinedTextField(
            modifier = Modifier.constrainAs(passwordField) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }.fillMaxWidth().padding(horizontal = SPACING_24.dp),
            value = "",
            onValueChange = {}
        )
        AnimatedVisibility(
            modifier = Modifier.constrainAs(confirmField) {
                top.linkTo(passwordField.bottom, SPACING_16.dp)
            }.fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = SPACING_24.dp),
            visible = true
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {}
            )
        }
        OutlinedButton(
            modifier = Modifier.constrainAs(mainButton) {
                top.linkTo(confirmField.bottom, SPACING_16.dp)
            }.fillMaxWidth().padding(horizontal = SPACING_24.dp),
            onClick = {}
        ) {
            Text(text = "Login")
        }
        Box(
            modifier = Modifier.size(SPACING_8.dp).background(
                color = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(SPACING_4.dp)
            ).constrainAs(dot) {
                start.linkTo(vertCenter)
                end.linkTo(vertCenter)
                top.linkTo(termsLabel.top)
                bottom.linkTo(termsLabel.bottom)
                top.linkTo(policyLabel.top)
                bottom.linkTo(policyLabel.bottom)
            }
        )
        Text(
            modifier = Modifier.constrainAs(stateButton) {
                bottom.linkTo(parent.bottom, SPACING_8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = "Have account?"
        )
        Text(
            modifier = Modifier.constrainAs(termsLabel) {
                bottom.linkTo(stateButton.top, SPACING_8.dp)
                end.linkTo(dot.start, SPACING_4.dp)
            },
            text = "Terms of Use"
        )
        Text(
            modifier = Modifier.constrainAs(policyLabel) {

                bottom.linkTo(stateButton.top, SPACING_8.dp)
                start.linkTo(dot.end, SPACING_4.dp)
            },
            text = "Privacy Policy"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() = MyApplicationTheme {
    LoginScreen()
}
