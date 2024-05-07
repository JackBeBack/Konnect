package data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import viewmodels.DesktopSettings

// platform Module
val platformModule = module {
    single{ DesktopSettings()}
}