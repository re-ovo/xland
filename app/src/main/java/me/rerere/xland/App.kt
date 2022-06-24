package me.rerere.xland

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.rerere.compose_setting.preference.initComposeSetting

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initComposeSetting()
    }
}