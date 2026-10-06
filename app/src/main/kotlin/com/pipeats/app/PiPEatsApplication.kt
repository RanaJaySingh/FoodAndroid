package com.pipeats.app

import android.app.Application
import com.pipeats.app.data.local.DataStoreManager

class PiPEatsApplication : Application() {
    
    lateinit var dataStoreManager: DataStoreManager
        private set
    
    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager(this)
    }
}
