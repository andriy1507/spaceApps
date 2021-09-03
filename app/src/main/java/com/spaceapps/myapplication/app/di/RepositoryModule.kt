package com.spaceapps.myapplication.app.di

import com.spaceapps.myapplication.app.repositories.auth.AuthRepository
import com.spaceapps.myapplication.app.repositories.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
