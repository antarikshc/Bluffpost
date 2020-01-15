package com.antarikshc.bluffpost.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Content(

    val headline: String,

    val byline: String?,

    @ColumnInfo(name = "thumbnail_url")
    @SerializedName("thumbnail")
    val thumbnailUrl: String

) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Content

        if (headline != other.headline) return false
        if (byline != other.byline) return false
        if (thumbnailUrl != other.thumbnailUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headline.hashCode()
        result = 31 * result + byline.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        return result
    }
}