package com.spaceapps.myapplication.core.di

import android.content.Context
import com.spaceapps.myapplication.core.di.modules.NetworkModule
import com.spaceapps.myapplication.core.di.modules.RepositoryModule
import com.spaceapps.myapplication.core.di.modules.RoomModule
import com.spaceapps.myapplication.core.di.modules.UtilsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        RoomModule::class,
        UtilsModule::class
    ]
)
interface CoreComponent : CoreEntryPoint {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): CoreComponent
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context): CoreEntryPoint {
            return DaggerCoreComponent.builder()
                .context(context)
                .build()
        }
    }
}
