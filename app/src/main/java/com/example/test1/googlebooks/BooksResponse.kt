package com.example.test1.googlebooks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BooksResponse(
    @SerialName("items"      ) var items      : List<Items> = emptyList()
)

@Serializable
data class Items (
    @SerialName("volumeInfo" ) var volumeInfo : VolumeInfo? = VolumeInfo()
)

@Serializable
data class VolumeInfo(
    @SerialName("industryIdentifiers" ) var industryIdentifiers : ArrayList<IndustryIdentifiers> = arrayListOf(),
    @SerialName("title") var title: String? = null,
    @SerialName("authors") var authors: ArrayList<String> = arrayListOf(),
    @SerialName("pageCount") var pageCount: Int? = null,
    @SerialName("imageLinks"          ) var imageLinks          : ImageLinks?                    = ImageLinks(),
)

@Serializable
data class IndustryIdentifiers (

    @SerialName("type"       ) var type       : String? = null,
    @SerialName("identifier" ) var identifier : String? = null

)

@Serializable
data class ImageLinks (
    @SerialName("thumbnail"      ) var thumbnail      : String? = null

)