import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.a
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.h2
import kotlinx.html.js.li
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.ul
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLVideoElement


data class Chapter(val time: Int, val title: String, val page: Int) {
    val id = ++COUNTER

    companion object {
        var COUNTER = 0
    }
}

val chapters = listOf(
    Chapter(0, "Chapter 0", 1),
    Chapter(10, "Chapter A", 2),
    Chapter(20, "Chapter B", 5),
    Chapter(30, "Chapter C", 10),
    Chapter(40, "Chapter D", 15),
    Chapter(50, "Chapter E", 1)
)

val pageMap =
    listOf(
        0 to 1,
        10 to 2,
        12 to 3,
        13 to 4,
        14 to 4,
    )


fun main() {
    window.onload = {
        LectureViewer()
    }
}

class LectureViewer {
    private var loadedPdf: PDFDocumentProxy? = null
    private val videoPlayer: HTMLVideoElement = document.getElementById("player") as HTMLVideoElement
    private val divChapters: HTMLDivElement = document.getElementById("chapters") as HTMLDivElement
    private val divPdf: HTMLDivElement = document.getElementById("pdf") as HTMLDivElement
    private val canvasPdf: HTMLCanvasElement = document.getElementById("the-canvas") as HTMLCanvasElement;


    init {
        initializeChapters()
        initializeVideoPlayer()
        initializePdf()
    }

    private fun initializePdf() {
        val loadingTask: PDFLoadingTask<PDFDocumentProxy> = getDocument("test.pdf");
        loadingTask.promise.then({ pdf ->
            this.loadedPdf = pdf
        })
    }

    private fun initializeChapters() {
        //clear
        divChapters.append {
            h2 { +"Chapters" }
            ul {
                chapters.forEach { chap ->
                    li {
                        this.id = "chapter-mark-${chap.id}"
                        val min = chap.time / 60
                        val sec = chap.time % 60
                        a("#") {
                            +"$min:$sec ${chap.title} (page: ${chap.page})"
                            onClickFunction = { jumpToChapter(chap) }
                        }
                    }
                }
            }
        }
    }

    private fun jumpToChapter(chap: Chapter) {
        jumpToTime(chap.time)
        jumpToPage(chap.page)
    }

    private fun jumpToPage(page: Int) {
        loadPdfPage(page)
    }

    private fun jumpToTime(time: Int) {
        videoPlayer.currentTime = time.toDouble()
    }

    private fun initializeVideoPlayer() {
        videoPlayer.ontimeupdate = { updateTime() }

    }

    private fun updateTime() {
        val currentTime = videoPlayer.currentTime.toInt()
        pageMap.find { it.first == currentTime }?.also { (_, page) ->
            loadPdfPage(page)
        }
        console.log("Time: $currentTime")
    }

    private fun loadPdfPage(pageNumber: Int) {
        loadedPdf?.getPage(pageNumber)?.then({ page ->
            val defaultScale = 2
            val viewport = page.getViewport(object : ViewportParameters {
                override var scale: Number = defaultScale
            })
            val context = canvasPdf.getContext("2d")
            canvasPdf.height = viewport.height.toInt()
            canvasPdf.width = viewport.width.toInt()

            val renderContext = object : PDFRenderParams {
                override var canvasContext: CanvasRenderingContext2D = context as CanvasRenderingContext2D
                override var viewport: PDFPageViewport? = viewport
            }
            page.render(renderContext)
        })
    }

}
