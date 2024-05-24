package com.marioloncar.data.users.di

import com.marioloncar.data.users.data.UsersMockRepositoryImpl
import com.marioloncar.data.users.domain.UsersMockRepository
import com.marioloncar.data.users.domain.usecase.GetUsersUseCase
import org.koin.dsl.module

val usersDataModule = module {

    single<UsersMockRepository> { UsersMockRepositoryImpl() }

    single { GetUsersUseCase(usersMockRepository = get()) }
}
