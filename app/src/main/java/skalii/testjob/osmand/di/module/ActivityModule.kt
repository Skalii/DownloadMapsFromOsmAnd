package skalii.testjob.osmand.di.module


import android.content.Context

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

import skalii.testjob.osmand.ui.activity.MainActivity


@Module
class ActivityModule(private val activity: MainActivity) {

    @[Provides Singleton]
    fun provideApplicationContext(): Context = activity.applicationContext

}