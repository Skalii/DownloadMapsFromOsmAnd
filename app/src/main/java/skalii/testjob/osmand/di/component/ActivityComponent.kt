package skalii.testjob.osmand.di.component


import android.content.Context
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar

import dagger.Component

import javax.inject.Singleton

import skalii.testjob.osmand.di.module.ActivityModule
import skalii.testjob.osmand.di.module.ViewModule


@Component(modules = [ActivityModule::class, ViewModule::class])
@Singleton
interface ActivityComponent {

    fun getContext(): Context
    fun getProgressBar(): ContentLoadingProgressBar
    fun getProgressText(): TextView

}