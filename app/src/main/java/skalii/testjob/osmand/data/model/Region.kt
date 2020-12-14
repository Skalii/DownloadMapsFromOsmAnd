package skalii.testjob.osmand.data.model


import android.content.Context

import java.io.Serializable
import java.util.Locale.ROOT
import java.io.File

import org.w3c.dom.NamedNodeMap


data class Region(
    var name: String = "",
    var prefix: String? = null,
    var suffix: String? = null,
    var map: String? = null,
    var hasChild: Boolean? = null
) : Serializable {

    companion object {
        const val FILE_EXTENSION = "_2.obf.zip"
    }


    constructor(
        attributes: NamedNodeMap,
        prefix: String?,
        suffix: String?,
        hasChild: Boolean
    ) : this(
        name = attributes.getNamedItem("name").nodeValue,
        hasChild = hasChild
    ) {
        try {
            this.prefix = attributes.getNamedItem("inner_download_prefix").nodeValue
        } catch (ignored: NullPointerException) {
            this.prefix = prefix
        }

        try {
            this.suffix = attributes.getNamedItem("inner_download_suffix").nodeValue
        } catch (ignored: NullPointerException) {
            this.suffix = suffix
        }

        try {
            this.map = attributes.getNamedItem("map").nodeValue
        } catch (ignored: NullPointerException) {
        }
    }


    fun createFileName() = "${
        if (prefix != null && prefix?.isNotBlank() == true && prefix?.isNotEmpty() == true && prefix != "\$name") "${prefix}_" else ""
    }${name}${
        if (suffix != null && suffix?.isNotBlank() == true && suffix?.isNotEmpty() == true) "_$suffix" else ""
    }".capitalize(ROOT)

    fun checkFileExists(context: Context) =
        File(
            "${context.filesDir?.path}${File.separator}${createFileName()}$FILE_EXTENSION"
        ).exists()

}