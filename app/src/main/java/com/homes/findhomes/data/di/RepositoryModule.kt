package com.homes.findhomes.data.di

import com.homes.findhomes.data.repository.LogInRepositoryImpl
import com.homes.findhomes.data.repository.SearchRepositoryImpl
import com.homes.findhomes.data.repository.WishRepositoryImpl
import com.homes.findhomes.domain.repository.LogInRepository
import com.homes.findhomes.domain.repository.SearchRepository
import com.homes.findhomes.domain.repository.WishRepository
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
