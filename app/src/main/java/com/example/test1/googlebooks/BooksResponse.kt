package com.example.test1.googlebooks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BooksResponse(
    @SerialName("kind"       ) var kind       : String?          = null//,
    //@SerialName("totalItems" ) var totalItems : Int?             = null,
    //@SerialName("items"      ) var items      : List<Items> = emptyList()
)

@Serializable
data class Items (
    @SerialName("id"         ) var id         : String?     = null,
    @SerialName("volumeInfo" ) var volumeInfo : VolumeInfo? = VolumeInfo()
)

@Serializable
data class VolumeInfo(
    @SerialName("title") var title: String? = null,
    @SerialName("authors") var authors: ArrayList<String> = arrayListOf(),
    @SerialName("pageCount") var pageCount: Int? = null
)