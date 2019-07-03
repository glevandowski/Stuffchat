package com.levandowski

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.levandowski.di.ApplicationComponent
import com.levandowski.di.DaggerApplicationComponent
import com.levandowski.di.module.ApplicationModule
import com.levandowski.utils.AppConstants

class StuffChatApplication : Application() {

    companion object {
        const val DATA_BASE_NAME = AppConstants.DB_NAME
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = createApplicationComponent
    }

    private val createApplicationComponent
        get() = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

}
