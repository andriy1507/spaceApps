package com.spaceapps.myapplication.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.spaceapps.myapplication.ui.SpaceAppsTheme

open class ComposableFragment : Fragment() {

    @Composable
    open fun Content() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (view is ComposeView) view.setContent {
            SpaceAppsTheme {
                Content()
            }
        }
    }
}
