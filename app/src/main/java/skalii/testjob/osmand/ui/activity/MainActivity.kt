package skalii.testjob.osmand.ui.activity


import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

import by.kirich1409.viewbindingdelegate.viewBinding

import java.io.File
import java.math.RoundingMode

import skalii.testjob.osmand.R
import skalii.testjob.osmand.databinding.ActivityMainBinding
import skalii.testjob.osmand.di.component.ActivityComponent
import skalii.testjob.osmand.di.component.DaggerActivityComponent
import skalii.testjob.osmand.di.module.ActivityModule
import skalii.testjob.osmand.di.module.ViewModule


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind, R.id.activity_main)


    companion object {

        private lateinit var activityComponent: ActivityComponent

        fun getActivityComponent() = activityComponent

        fun updateDeviceMemory() {

            val totalSize =
                File(activityComponent.getContext().filesDir.absoluteFile.toString()).totalSpace.toDouble()
            val totalGb = totalSize / (1024 * 1024 * 1024)

            val freeSize =
                File(activityComponent.getContext().filesDir.absoluteFile.toString()).freeSpace.toDouble()
            val freeGb = freeSize / (1024 * 1024 * 1024)

            activityComponent.getProgressBar().max = totalGb.toInt()
            activityComponent.getProgressBar().progress = totalGb.toInt() - freeGb.toInt()
            activityComponent.getProgressText().text = "Free ${
                freeGb.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            } Gb"
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar
            ?.setBackgroundDrawable(
                ColorDrawable(
                    ResourcesCompat.getColor(resources, R.color.action_bar, null)
                )
            )

        activityComponent = DaggerActivityComponent
            .builder()
            .activityModule(ActivityModule(this))
            .viewModule(
                ViewModule(
                    viewBinding.progressMemoryActivityMain,
                    viewBinding.textFreeMemoryActivityMain
                )
            )
            .build()

        viewBinding.progressMemoryActivityMain.show()
        updateDeviceMemory()

    }

}