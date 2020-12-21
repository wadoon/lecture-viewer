import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.a
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.h2
import kotlinx.html.js.li
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.ul
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.url.URLSearchParams
import kotlin.math.abs
import kotlin.math.floor

fun getLectureResourceFile(): String? {
    val split = window.location.href.split("?")
    if (split.size > 1) {
        val paramString = split[1]
        val queryString = URLSearchParams(paramString)
        return queryString.get("lecture")
    }
    return null
}

fun fetchLecture(url: String, callback: (Lecture) -> Unit) {
    window.fetch(url).then { response ->
        response.text().then { text ->
            println("Parse json")
            println(text)
            val l: Lecture = Json.decodeFromString(text)
            callback(l)
        }
    }
        .catch {
            println("Error loading json file")
            throw it
        }
    println("Loading lecture resource $url")
}

fun main() {
    window.onload = {
        val lecture = getLectureResourceFile()
            ?.let {
                fetchLecture(it) {
                    LectureViewer(it)
                }
            }

        if (lecture == null) {
            //window.alert("No lecture URL given.")
            // for debugging
            window.location.href += "?lecture=test.json"
        }
    }
}

class LectureViewer(val data: Lecture) {
    private var loadedPdf: PDFDocumentProxy? = null
    private val videoPlayer: HTMLVideoElement = document.getElementById("player") as HTMLVideoElement
    private val divChapters: HTMLDivElement = document.getElementById("chapters") as HTMLDivElement
    private val divPdf: HTMLDivElement = document.getElementById("pdf") as HTMLDivElement
    private val canvasPdf: HTMLCanvasElement = document.getElementById("slides") as HTMLCanvasElement;
    private val canvasMouse: HTMLCanvasElement = document.getElementById("mouse") as HTMLCanvasElement;
    private val canvasSocial: HTMLCanvasElement = document.getElementById("social") as HTMLCanvasElement;
    private val mousePainter = MousePointerPainter(canvasMouse)


    init {
        initializePageMap()
        initializeChapters()
        initializeVideoPlayer()
        initializePdf()
        window.onresize = { relayout() }
    }

    private fun relayout() {
        val width = window.innerWidth
        val height = window.innerHeight
        val videoRatio = 1280.0 / 760.0
        //TODO calculate the new sizes

        canvasMouse.width = canvasPdf.width
        canvasSocial.width = canvasPdf.width
        canvasMouse.height = canvasPdf.height
        canvasSocial.height = canvasPdf.height
    }

    private fun initializePageMap() {
        println(data.pageMap)
        if (data.pageMap.isNotEmpty()) return
        val offset = data.eventData.offset
        var lastValue = 0;
        data.eventData.events.forEach {
            if (it.cmd == "slide") {
                val stamp = (it.stamp + offset) / 1000
                ((lastValue + 1)..stamp).forEach { time ->
                    data.pageMap.add(it.no!! to time)
                }
                lastValue = stamp
            }
        }
    }

    private fun initializePdf() {
        println("Load pdf: $data.pdfUrl")
        val loadingTask: PDFLoadingTask<PDFDocumentProxy> = getDocument(data.pdfUrl);
        loadingTask.promise.then({ pdf ->
            this.loadedPdf = pdf
            println("Pdf loaded")
            jumpToPage(1)

            if (data.chapters.isEmpty()) {
                fun asList(outline: Array<PDFTreeNode>?, depth: Int): MutableList<Chapter> {
                    if (outline == null) return mutableListOf()
                    return outline.flatMap { it ->
                        val seq = asList(it.items, depth + 1)
                        seq.add(0, Chapter(("&nbsp;" * depth) + it.title, it.dest as Int))
                        seq
                    }.toMutableList()
                }

                pdf.getOutline().then({ outline ->
                    data.chapters.addAll(asList(outline, 0))
                    initializeChapters()
                })
            }
        })
    }


    private fun initializeChapters() {
        val chapters = data.chapters
        divChapters.append {
            h2 { +"Chapters" }
            ul {
                chapters.forEach { chap ->
                    li {
                        this.id = "chapter-mark-${chap.hashCode()}"
                        //val min = chap.time / 60
                        //val sec = chap.time % 60
                        a("#") {
                            +"${chap.title} (page: ${chap.page})"
                            onClickFunction = { jumpToChapter(chap) }
                        }
                    }
                }
            }
        }
    }

    private fun jumpToChapter(chap: Chapter) {
        //jumpToTime(chap.time)
        jumpToPage(chap.page)
    }

    private fun jumpToPage(page: Int) {
        loadPdfPage(page)
    }

    private fun jumpToTime(time: Int) {
        videoPlayer.currentTime = time.toDouble()
    }

    private fun initializeVideoPlayer() {
        videoPlayer.src = data.videoUrl
        videoPlayer.ontimeupdate = { onTimeUpdated() }
    }

    var lastTime: Double = -1.0

    private fun onTimeUpdated(currentTime: Double = videoPlayer.currentTime) {
        println("Time: $currentTime")
        if (abs(lastTime - currentTime) < 0.1) return

        data.pageMap.find { it.first == currentTime.toInt() }?.also { (_, page) ->
            loadPdfPage(page)
        }

        val currentTimeMs = floor(currentTime * 1000).toInt()
        data.eventData.events.find { it.stamp >= currentTimeMs }
            ?.let {
                println("Found command: $it")
                interpretEventCommand(it)
            }
    }

    //needed for repainting the canvas
    var currentPage = -1

    private fun loadPdfPage(pageNumber: Int) {
        println("Load page $pageNumber")
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

    /*
    fun update() {
        var newStamp = video.currentTime * 1000 - offset;
        if (newStamp < 0) newStamp = 0;

        if (newStamp < lastStamp) {
            currentIndex = 0;
        }

        while (newStamp > data[currentIndex].stamp) {
            interpret(data[currentIndex]);
            currentIndex++;
        }

        document.getElementById("xxx")?.innerHTML = "$newStamp / $currentIndex # $lastStamp";
        lastStamp = newStamp;
    }*/

    fun interpretEventCommand(element: Event) {
        when (element.cmd) {
            "slide" -> jumpToPage(element.no!!)
            "mode" -> mousePainter.enable = element.mode == "pointer"
            "move" -> mousePainter.moveTo(element.x!!, element.y!!)
            //p.style.left = element.x * 720 - 20;
            //p.style.top = element.y * 540 - 20;
        }
    }
}

private operator fun String.times(depth: Int): String {
    var s = ""
    (1..depth).forEach { s += this }
    return s
}

class MousePointerPainter(val canvas: HTMLCanvasElement) {
    var enable: Boolean = false
        set(value) {
            println("Mouse pointer enabled")
            field = value
            if (!value) clearCanvas()
        }

    //var x: Double = 0.0
    //var y: Double = 0.0

    init {

    }

    fun moveTo(x: Double, y: Double) {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.save()
        //context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
        context.beginPath()
        val xPage = x * canvas.width
        val yPage = y * canvas.height
        context.fillStyle = "rgba(255,0,0,0.3)"
        context.shadowBlur = 10.0
        context.arc(xPage, yPage, 10.0, 0.0, 2 * kotlin.math.PI)
        context.fill()
        context.restore()
    }


    private fun clearCanvas() {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }
}

