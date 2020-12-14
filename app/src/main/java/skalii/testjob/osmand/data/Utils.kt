package skalii.testjob.osmand.data


import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.io.File

import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import skalii.testjob.osmand.data.model.Region
import skalii.testjob.osmand.data.model.Region.Companion.FILE_EXTENSION
import skalii.testjob.osmand.data.remote.RemoteService
import skalii.testjob.osmand.saveFileToExternalStorage


object Utils {

    fun deleteMap(
        context: Context,
        fileName: String,
        runAnyAction: (Boolean) -> Unit
    ) {
        runAnyAction(
            File(
                "${context.filesDir?.path}${File.separator}${fileName}$FILE_EXTENSION"
            ).delete()
        )
    }

    fun saveMap(
        context: Context,
        fileName: String,
        runAnyAction: (Boolean) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            runAnyAction(
                RemoteService
                    .getServiceApi(context)
                    .getSomeZipFiles("${fileName}$FILE_EXTENSION")
                    ?.saveFileToExternalStorage(context, "${fileName}$FILE_EXTENSION")
                    ?.exists() ?: false
            )
        }
    }


    fun parseRegions(
        appContext: Context,
        parent: String = "",
        prefix: String? = null,
        suffix: String? = null
    ): List<Region> {
        val regions = mutableListOf<Region>()

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(appContext.assets.open("regions.xml"))
        val regionsList = document.documentElement

        iterateOverNodes(
            rootElement = regionsList,
            regions = regions,
            parent = parent,
            prefix = prefix,
            suffix = suffix
        )

        return regions
    }

    private fun iterateOverNodes(
        rootElement: Element? = null,
        regions: MutableList<Region> = mutableListOf(),
        parent: String = "",
        parentNode: Node? = null,
        childNodes: NodeList? = null,
        prefix: String? = null,
        suffix: String? = null
    ) {
        val currentNodes = rootElement?.childNodes ?: parentNode?.childNodes ?: childNodes

        if (currentNodes != null) {
            for (i in 0 until currentNodes.length) {
                if (currentNodes.item(i).nodeName == "region") {

                    val node = currentNodes.item(i)
                    val attributes = node.attributes

                    if (parent.isNotEmpty() && parent.isNotBlank()) {

                        if (parent == attributes.getNamedItem("name").nodeValue) {
                            iterateOverNodes(
                                regions = regions,
                                parentNode = node,
                                prefix = prefix,
                                suffix = suffix
                            )
                        } else {
                            iterateOverNodes(
                                regions = regions,
                                parent = parent,
                                childNodes = node.childNodes,
                                prefix = prefix,
                                suffix = suffix
                            )
                        }

                    } else {
                        regions.add(
                            Region(
                                attributes,
                                prefix,
                                suffix,
                                node.hasChildNodes()
                            )
                        )
                    }
                }
            }
        }

    }

}