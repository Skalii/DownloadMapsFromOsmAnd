package skalii.testjob.osmand.di.module


import android.widget.TextView

import androidx.core.widget.ContentLoadingProgressBar

import dagger.Module
import dagger.Provides

import javax.inject.Singleton


@Module
class ViewModule(
    private val progressBar: ContentLoadingProgressBar,
    private val progressText: TextView
) {

    @[Provides Singleton]
    fun provideProgressBar(): ContentLoadingProgressBar = progressBar

    @[Provides Singleton]
    fun provideProgressText(): TextView = progressText

}