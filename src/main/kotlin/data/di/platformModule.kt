package data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodels.SettingsViewModel

// platform Module
val platformModule = module {
    singleOf(::SettingsViewModel)
}