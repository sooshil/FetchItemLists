package com.sukajee.fetchlist

import android.app.Application
import com.sukajee.itemlist.di.initKoin
import org.koin.android.ext.koin.androidContext

class ItemsApplications : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ItemsApplications)
        }
    }
}