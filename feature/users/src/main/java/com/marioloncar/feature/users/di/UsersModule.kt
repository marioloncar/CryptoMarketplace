package com.marioloncar.feature.users.di

import com.marioloncar.feature.users.presentation.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val usersModule = module {

    viewModelOf(::UsersViewModel)
}
