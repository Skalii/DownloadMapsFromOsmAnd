package skalii.testjob.osmand.ui.fragment


import android.os.Bundle
import android.view.View

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

import by.kirich1409.viewbindingdelegate.viewBinding

import skalii.testjob.osmand.R
import skalii.testjob.osmand.data.Utils.parseRegions
import skalii.testjob.osmand.data.model.Region
import skalii.testjob.osmand.databinding.FragmentRegionBinding
import skalii.testjob.osmand.setCustomVertical
import skalii.testjob.osmand.ui.adapter.RegionAdapter


class RegionFragment : Fragment(R.layout.fragment_region) {

    private val viewBinding by viewBinding(FragmentRegionBinding::bind)
    private val args: RegionFragmentArgs by navArgs()
    private var region: Region? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        region = args.region

        val regionAdapter = RegionAdapter()
        regionAdapter.setData(
            parseRegions(
                region?.name ?: "europe",
                if (region?.prefix == "\$name") region?.name else region?.prefix,
                region?.suffix ?: "europe"
            )
        )
        viewBinding.recyclerFragmentRegion.setCustomVertical(requireContext(), regionAdapter)
    }

}