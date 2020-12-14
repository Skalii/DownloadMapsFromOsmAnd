package skalii.testjob.osmand.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import java.util.Locale.ROOT

import skalii.testjob.osmand.R
import skalii.testjob.osmand.data.Utils.deleteMap
import skalii.testjob.osmand.data.Utils.saveMap
import skalii.testjob.osmand.data.model.Region
import skalii.testjob.osmand.databinding.ItemRegionBinding
import skalii.testjob.osmand.toast
import skalii.testjob.osmand.ui.adapter.RegionAdapter.RegionViewHolder
import skalii.testjob.osmand.ui.fragment.RegionFragmentDirections


class RegionAdapter : RecyclerView.Adapter<RegionViewHolder>() {

    private val data = mutableListOf<Region>()


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        RegionViewHolder(
            LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.item_region, viewGroup, false)
        )

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        data[position].let { holder.bind(it) }
    }


    fun setData(newData: List<Region>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


    inner class RegionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemRegionBinding.bind(view)

        private val item = viewBinding.itemRegion
        private val imageMap = viewBinding.imageMapItemRegion
        private val textRegion = viewBinding.textRegionItemRegion
        private val imageStatus = viewBinding.imageStatusItemRegion


        fun bind(region: Region) {

            textRegion.text = region.name
                .capitalize(ROOT)
                .replace("-", " ")
                .replace("_", " ")

            var status = region.checkFileExists(viewBinding.root.context)

            setImageMap(status)
            setImageStatus(status)

            imageStatus.setOnClickListener {
                if (region.checkFileExists(viewBinding.root.context)) {
                    deleteMap(
                        viewBinding.root.context,
                        region.createFileName()
                    ) { exists ->
                        status = exists
                        setImageMap(!status)
                        setImageStatus(!status)
                        if (exists) it.context.toast("Карта удалена")
                    }
                } else {
                    it.context.toast("Загрузка карты началась")
                    saveMap(
                        viewBinding.root.context,
                        region.createFileName()
                    ) { exists ->
                        status = exists
                        setImageMap(status)
                        setImageStatus(status)
                        showStatus(status)
                    }
                }
            }

            if (region.hasChild == true) {
                item.setOnClickListener {
                    val directions = RegionFragmentDirections.actionRegionFragmentSelf(region)
                    it.findNavController().navigate(directions)
                }
            }
        }

        private fun setImageMap(status: Boolean) {
            imageMap.setColorFilter(
                ContextCompat.getColor(
                    viewBinding.root.context,
                    if (status) R.color.status_true else R.color.status_false
                )
            )
        }

        private fun setImageStatus(status: Boolean) {
            imageStatus.setImageResource(
                if (status) R.mipmap.ic_action_remove_dark else R.mipmap.ic_action_import
            )
        }

        private fun showStatus(status: Boolean) {
            viewBinding.root.context.toast(
                if (status) "Карта загружена"
                else """Возможно карты на данный момент нет.
                             |Повторите попытку позже.""".trimMargin()
            )
        }

    }

}