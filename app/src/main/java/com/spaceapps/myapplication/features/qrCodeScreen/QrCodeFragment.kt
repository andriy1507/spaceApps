package com.spaceapps.myapplication.features.qrCodeScreen

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrCodeFragment : ComposableFragment() {

    private val vm by viewModels<QrCodeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.onUserInput("Test QR-Code for space apps")
        vm.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant))
    }

    @Composable
    override fun Content() = QrCodeScreen(vm = vm)
}
