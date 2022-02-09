package com.spaceapps.myapplication.features.auth

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.app.SpaceAppsApplication
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ApplicationTest {

    @Test
    fun check_package_name() {
        val context = ApplicationProvider.getApplicationContext<SpaceAppsApplication>()
        Truth.assertThat(context.packageName).isEqualTo(BuildConfig.APPLICATION_ID)
    }
}
