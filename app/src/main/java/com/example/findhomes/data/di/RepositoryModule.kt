package com.example.findhomes.data.di

import com.example.findhomes.data.repository.LogInRepositoryImpl
import com.example.findhomes.data.repository.SearchRepositoryImpl
import com.example.findhomes.data.repository.WishRepositoryImpl
import com.example.findhomes.domain.repository.LogInRepository
import com.example.findhomes.domain.repository.SearchRepository
import com.example.findhomes.domain.repository.WishRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepository: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindLogInRepository(
        logInRepositoryImpl: LogInRepositoryImpl
    ) : LogInRepository

    @Binds
    @Singleton
    abstract fun bindingWishRepository(
        wishRepositoryImpl: WishRepositoryImpl
    ) : WishRepository
}
