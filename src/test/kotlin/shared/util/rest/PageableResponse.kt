package shared.util.rest

import shared.util.serialization.gson

data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val pageable: PageableData = PageableData(),
    val last: Boolean = true,
    val totalPages: Long = 1,
    val totalElements: Long = 0,
    val first: Boolean = true,
    val size: Long = 0,
    val number: Long = 0,
    val sort: SortData = SortData(),
    val numberOfElements: Long = 0,
    val empty: Boolean = false,
) {
    fun toJson() = gson.toJson(this)
}

data class PageableData(
    val pageNumber: Long = 0,
    val pageSize: Long = 20,
    val sort: SortData = SortData(),
    val offset: Long = 0,
    val paged: Boolean = true,
    val unpaged: Boolean = false,
)

data class SortData(
    val empty: Boolean = true,
    val sorted: Boolean = true,
    val unsorted: Boolean = false,
)

fun String.contentSubstring() = substring(
    indexOf("[{"),
    lastIndexOf("}]") + 2,
)
