package com.levandowski.di

import com.levandowski.adapter.chat.ChatAdapter
import com.levandowski.data.repository.MessageRepository
import com.levandowski.data.repository.UserRepository
import com.levandowski.di.module.ApplicationModule
import com.levandowski.di.module.AuthModule
import com.levandowski.di.module.DataModule
import com.levandowski.ui.chat.ChatViewModel
import com.levandowski.ui.main.MainActivity
import com.levandowski.ui.main.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class, AuthModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(chatViewModel: ChatViewModel)

    fun inject(userRepository: UserRepository)

    fun inject(messageRepository: MessageRepository)

    fun inject(mainActivityViewModel: MainActivityViewModel)

    fun inject(chatAdapter: ChatAdapter)
}
