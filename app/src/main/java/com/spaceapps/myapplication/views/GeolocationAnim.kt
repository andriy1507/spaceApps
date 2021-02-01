package com.spaceapps.myapplication.views

import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_48
import com.spaceapps.myapplication.views.GeolocationAnim.State.Final
import com.spaceapps.myapplication.views.GeolocationAnim.State.Init
import com.spaceapps.myapplication.views.GeolocationAnim.definition
import com.spaceapps.myapplication.views.GeolocationAnim.pulseProp
import com.spaceapps.myapplication.views.GeolocationAnim.rotationProp

@Composable
fun PulseRecordIcon() {
    val transition = transition(
        definition = definition,
        initState = Init,
        toState = Final
    )
    val iconAlpha = transition[pulseProp]
    Image(
        modifier = Modifier.size(SPACING_48.dp),
        alpha = iconAlpha,
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
        imageVector = vectorResource(R.drawable.ic_recording)
    )
}

@Composable
fun LoaderIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary
) {
    val bgAnim = transition(
        definition = definition,
        initState = Init,
        toState = Final
    )
    val rotation = bgAnim[rotationProp]
    Icon(
        imageVector = vectorResource(id = R.drawable.ic_loading),
        modifier = modifier.fillMaxSize().rotate(rotation).size(ACTION_BAR_SIZE.dp),
        tint = color
    )
}

object GeolocationAnim {

    sealed class State {
        object Init : State()
        object Final : State()
    }

    private const val DURATION = 1000
    val rotationProp = FloatPropKey(label = "loaderRotation")
    val pulseProp = FloatPropKey(label = "pulseAlpha")
    val definition = transitionDefinition<State> {
        state(Init) {
            this[rotationProp] = 0f
            this[pulseProp] = 0f
        }
        state(Final) {
            this[rotationProp] = 360f
            this[pulseProp] = 1f
        }
        transition(
            Init to Final
        ) {
            rotationProp using infiniteRepeatable(
                animation = tween(durationMillis = DURATION),
                repeatMode = RepeatMode.Restart
            )
            pulseProp using infiniteRepeatable(
                animation = tween(durationMillis = DURATION / 2),
                repeatMode = RepeatMode.Reverse
            )
        }
    }
}
