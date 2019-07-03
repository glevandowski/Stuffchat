package com.levandowski.di.module

import android.content.Context
import androidx.room.Room
import com.levandowski.StuffChatApplication
import com.levandowski.data.database.StuffDataBase
import com.levandowski.data.preferences.AppPreferencesHelper
import com.levandowski.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): StuffDataBase {
        return Room.databaseBuilder(context, StuffDataBase::class.java,
            StuffChatApplication.DATA_BASE_NAME).build()
    }

    @Singleton
    @Provides
    fun providePreferences(context: Context): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }
}