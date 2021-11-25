package com.spaceapps.myapplication.ui.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_8

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun GooglePayButton(
    modifier: Modifier = Modifier
) {
    GooglePayTheme {
        Button(
            modifier = Modifier
                .padding(SPACING_8)
                .then(modifier)
                .defaultMinSize(minWidth = 152.dp),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(contentColor = Color.Unspecified)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.googlepay_button_content),
                contentDescription = null
            )
        }
    }
}

@Composable
fun GooglePayTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) {
            lightColors(primary = Color.White)
        } else {
            darkColors(primary = Color.Black)
        },
        content = content
    )
}

@Composable
fun rememberPaymentsClient(): PaymentsClient {
    val context = LocalContext.current
    val walletOptions = Wallet.WalletOptions.Builder()
        .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
    return remember { Wallet.getPaymentsClient(context, walletOptions) }
}
