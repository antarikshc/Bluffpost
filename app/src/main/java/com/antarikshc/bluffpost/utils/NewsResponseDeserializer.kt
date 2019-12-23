package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.models.NewsResponse
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class NewsResponseDeserializer : JsonDeserializer<NewsResponse> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): NewsResponse {
        // Get the "content" element from the parsed JSON
        val content = json?.asJsonObject?.get("response")

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        return Gson().fromJson<NewsResponse>(content, typeOfT)
    }

}