package com.spaceapps.myapplication.core.di

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@DefineComponent(parent = SingletonComponent::class)
interface CoreComponent {

    @DefineComponent.Builder
    interface Builder {
        fun build(): CoreComponent
    }
}