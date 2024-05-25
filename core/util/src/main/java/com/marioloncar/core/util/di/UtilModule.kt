package com.marioloncar.core.util.di

import com.marioloncar.core.util.ticker.TickerUtil
import com.marioloncar.core.util.ticker.TickerUtilImpl
import org.koin.dsl.module

val utilModule = module {

    single<TickerUtil> { TickerUtilImpl() }
}
