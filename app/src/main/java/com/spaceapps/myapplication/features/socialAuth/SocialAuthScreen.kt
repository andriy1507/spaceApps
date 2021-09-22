package com.spaceapps.myapplication.features.socialAuth

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.*

@Composable
fun SocialAuthScreen() {
    Column {
        Button(
            modifier = Modifier
                .padding(start = SPACING_16, end = SPACING_16, top = SPACING_16, bottom = SPACING_8)
                .fillMaxWidth(),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = googleColor,
                contentColor = MaterialTheme.colors.onPrimary
            ),
            contentPadding = PaddingValues(SPACING_16)
        ) {
            Text(text = stringResource(id = R.string.continue_with_google))
            Spacer(modifier = Modifier.width(SPACING_8))
            Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = null)
        }
        Button(
            modifier = Modifier
                .padding(horizontal = SPACING_16, vertical = SPACING_8)
                .fillMaxWidth(),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = facebookColor,
                contentColor = MaterialTheme.colors.onPrimary
            ),
            contentPadding = PaddingValues(SPACING_16)
        ) {
            Text(text = stringResource(id = R.string.continue_with_facebook))
            Spacer(modifier = Modifier.width(SPACING_8))
            Icon(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = null)
        }
        Button(
            modifier = Modifier
                .padding(start = SPACING_16, end = SPACING_16, bottom = SPACING_16, top = SPACING_8)
                .fillMaxWidth(),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = appleColor,
                contentColor = MaterialTheme.colors.onPrimary
            ),
            contentPadding = PaddingValues(SPACING_16)
        ) {
            Text(text = stringResource(id = R.string.continue_with_apple_id))
            Spacer(modifier = Modifier.width(SPACING_8))
            Icon(painter = painterResource(id = R.drawable.ic_apple), contentDescription = null)
        }
    }
}
