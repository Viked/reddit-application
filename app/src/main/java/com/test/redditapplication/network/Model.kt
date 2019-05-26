package com.test.redditapplication.network

data class TopResponse(
    val `data`: TopListPage?
)

data class TopListPage(
    val children: List<LinkResponse>?
)

data class LinkResponse(
    val `data`: Link?
)

data class Link(
    val author: String?,
    val author_fullname: String?,
    val created: Double?,
    val created_utc: Double?,
    val id: String?,
    val name: String?,
    val num_comments: Int?,
    val preview: Preview?,
    val score: Int?,
    val thumbnail: String?,
    val title: String?,
    val url: String?,
    val view_count: Int?
)

data class Preview(
    val images: List<Image>?
)

data class Image(
    val id: String?,
    val source: Source?
)

data class Source(
    val height: Int?,
    val url: String?,
    val width: Int?
)