import kotlinx.serialization.Serializable

/**
 *
 * @author Alexander Weigl
 * @version 1 (12/9/20)
 */

@Serializable
data class Lecture(
    val videoUrl: String,
    val pdfUrl: String,
    val title: String,
    val chapters: MutableList<Chapter>,
    val eventData: EventData,
) {
    val pageMap: MutableList<Pair<Int, Int>> = mutableListOf()
}

@Serializable
data class EventData(
    /*Offset between video time and time of given events*/
    val offset: Int = 0,
    val events: List<Event>
)


@Serializable
data class Event(
    val stamp: Int,
    val cmd: String? = null,
    val no: Int? = null,
    val mode: String? = null,
    val x: Double? = null,
    val y: Double? = null,
)

@Serializable
data class Chapter(val title: String, val page: Int) {
    val id = title.hashCode()
}
