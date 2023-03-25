package com.smitch.DI

import android.app.Application
import android.content.Context
import android.net.nsd.NsdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideNsdManager(application: Application): NsdManager =
        application.getSystemService(Context.NSD_SERVICE) as NsdManager

}
