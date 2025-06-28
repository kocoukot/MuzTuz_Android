package com.artline.muztus.ui

import android.app.Application
import com.artline.muztus.di.authModule
import com.artline.muztus.di.domainModule
import com.artline.muztus.di.repositoryModule
import com.artline.muztus.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MuztusApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MuztusApplication)
            modules(authModule + repositoryModule + storageModule + domainModule)
        }
    }
}