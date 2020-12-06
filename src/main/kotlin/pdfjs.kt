@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

@file:JsModule("pdfjs-dist")
@file:JsNonModule

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.Uint8Array
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLElement
import kotlin.js.Promise

external var version: String

@Suppress("EXTERNAL_DELEGATION", "NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface GlobalWorkerOptions {
    var workerSrc: String

    companion object : GlobalWorkerOptions by definedExternally
}

external interface PDFPromise<T> {
    fun isResolved(): Boolean
    fun isRejected(): Boolean
    fun resolve(value: T)
    fun reject(reason: String)
    fun <U> then(onResolve: (promise: T) -> U, onReject: (reason: String) -> Unit = definedExternally): PDFPromise<U>
}

external interface PDFTreeNode {
    var title: String
    var bold: Boolean
    var italic: Boolean
    var color: Array<Number>
    var dest: Any
    var items: Array<PDFTreeNode>
}

external interface PDFInfo {
    var PDFFormatVersion: String
    var IsAcroFormPresent: Boolean
    var IsXFAPresent: Boolean
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
}

external interface PDFMetadata {
    fun parse()
    fun get(name: String): String
    fun has(name: String): Boolean
}

external interface PDFDataRangeTransportListener {
    @nativeInvoke
    operator fun invoke(loaded: Number, total: Number)
}

external enum class VerbosityLevel {
    ERRORS /* = 0 */,
    WARNINGS /* = 1 */,
    INFOS /* = 5 */
}

external open class PDFDataRangeTransport {
    constructor(length: Number, initialData: Uint8Array, progressiveDone: Boolean = definedExternally)
    constructor(length: Number, initialData: Uint8Array)
    constructor(length: Number, initialData: ArrayBufferView, progressiveDone: Boolean = definedExternally)
    constructor(length: Number, initialData: ArrayBufferView)
    constructor(length: Number, initialData: ArrayBuffer, progressiveDone: Boolean = definedExternally)
    constructor(length: Number, initialData: ArrayBuffer)
    open fun addRangeListener(listener: PDFDataRangeTransportListener)
    open fun addProgressListener(listener: PDFDataRangeTransportListener)
    open fun addProgressiveReadListener(listener: PDFDataRangeTransportListener)
    open fun addProgressiveDoneListener(listener: PDFDataRangeTransportListener)
    open fun onDataRange(begin: Number, chunk: Any)
    open fun onDataProgress(loaded: Number, total: Number)
    open fun onDataProgressiveRead(chunk: Any)
    open fun onDataProgressiveDone()
    open fun transportReady()
    open fun requestDataRange(begin: Number, end: Number)
    open fun abort()
}

external interface PDFWorkerParameters {
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var port: Any?
        get() = definedExternally
        set(value) = definedExternally
    var verbosity: VerbosityLevel?
        get() = definedExternally
        set(value) = definedExternally
}

open external class PDFWorker(params: PDFWorkerParameters = definedExternally) {
    open var promise: Promise<Any>
    open var port: Any?
    open var messageHandler: Any?
    open fun destroy()

    companion object {
        fun fromPort(params: PDFWorkerParameters = definedExternally): PDFWorker
        fun getWorkerSrc(): String
    }
}

external enum class CMapCompressionType {
    NONE /* = 0 */,
    BINARY /* = 1 */,
    STREAM /* = 2 */
}

external interface CMapReaderFactory

external interface `T$0` {
    var name: String
}

external interface `T$1` {
    var cMapData: Any
    var compressionType: CMapCompressionType
}

external interface CMapReader {
    fun fetch(params: `T$0`): Promise<`T$1`>
}

external interface `T$2` {
    @nativeGetter
    operator fun get(key: String): String?
    @nativeSetter
    operator fun set(key: String, value: String)
}

external interface PDFSource {
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var data: dynamic /* Uint8Array? | ArrayBufferView? | ArrayBuffer? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var httpHeaders: `T$2`?
        get() = definedExternally
        set(value) = definedExternally
    var password: String?
        get() = definedExternally
        set(value) = definedExternally
    var withCredentials: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var initialData: dynamic /* Uint8Array? | ArrayBufferView? | ArrayBuffer? */
        get() = definedExternally
        set(value) = definedExternally
    var length: Number?
        get() = definedExternally
        set(value) = definedExternally
    var range: PDFDataRangeTransport?
        get() = definedExternally
        set(value) = definedExternally
    var rangeChunkSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var worker: PDFWorker?
        get() = definedExternally
        set(value) = definedExternally
    var verbosity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var docBaseUrl: String?
        get() = definedExternally
        set(value) = definedExternally
    var nativeImageDecoderSupport: String? /* "decode" | "display" | "none" */
        get() = definedExternally
        set(value) = definedExternally
    var cMapUrl: String?
        get() = definedExternally
        set(value) = definedExternally
    var cMapPacked: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var CMapReaderFactory: Any?
        get() = definedExternally
        set(value) = definedExternally
    var stopAtErrors: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxImageSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var isEvalSupported: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableFontFace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableRange: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableStream: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableAutoFetch: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableCreateObjectURL: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var pdfBug: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PDFProgressData {
    var loaded: Number
    var total: Number
}

external interface `T$3` {
    var info: PDFInfo
    var metadata: PDFMetadata
}

external interface PDFDocumentProxy {
    var numPages: Number
    var fingerprint: String
    fun embeddedFontsUsed(): Boolean
    fun getPage(number: Number): PDFPromise<PDFPageProxy>
    fun getDestinations(): PDFPromise<Array<Any>>
    fun getJavaScript(): PDFPromise<Array<String>>
    fun getOutline(): PDFPromise<Array<PDFTreeNode>>
    fun getMetadata(): PDFPromise<`T$3`>
    fun isEncrypted(): PDFPromise<Boolean>
    fun getData(): PDFPromise<Uint8Array>
    fun dataLoaded(): PDFPromise<Array<Any>>
    fun destroy()
}

external interface PDFRef {
    var num: Number
    var gen: Any
}

external interface PDFPageViewportOptions {
    var viewBox: Any
    var scale: Number
    var rotation: Number
    var offsetX: Number
    var offsetY: Number
    var dontFlip: Boolean
}

external interface PDFPageViewport {
    var width: Number
    var height: Number
    var scale: Number
    var transforms: Array<Number>
    fun clone(options: PDFPageViewportOptions): PDFPageViewport
    fun convertToViewportPoint(x: Number, y: Number): Array<Number>
    fun convertToViewportRectangle(rect: Array<Number>): Array<Number>
    fun convertToPdfPoint(x: Number, y: Number): Array<Number>
}

external interface ViewportParameters {
    var scale: Number
    var rotation: Number?
        get() = definedExternally
        set(value) = definedExternally
    var offsetX: Number?
        get() = definedExternally
        set(value) = definedExternally
    var offsetY: Number?
        get() = definedExternally
        set(value) = definedExternally
    var dontFlip: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PDFAnnotationData {
    var subtype: String
    var rect: Array<Number>
    var annotationFlags: Any
    var color: Array<Number>
    var borderWidth: Number
    var hasAppearance: Boolean
}

external interface PDFAnnotations {
    fun getData(): PDFAnnotationData
    fun hasHtml(): Boolean
    fun getHtmlElement(commonOjbs: Any): HTMLElement
    fun getEmptyContainer(tagName: String, rect: Array<Number>): HTMLElement
    fun isViewable(): Boolean
    fun loadResources(keys: Any): PDFPromise<Any>
    fun getOperatorList(evaluator: Any): PDFPromise<Any>
}

external interface PDFRenderTextLayer {
    fun beginLayout()
    fun endLayout()
    fun appendText()
}

external interface PDFRenderImageLayer {
    fun beginLayout()
    fun endLayout()
    fun appendImage()
}

external interface PDFRenderParams {
    var canvasContext: CanvasRenderingContext2D
    var viewport: PDFPageViewport?
        get() = definedExternally
        set(value) = definedExternally
    var textLayer: PDFRenderTextLayer?
        get() = definedExternally
        set(value) = definedExternally
    var imageLayer: PDFRenderImageLayer?
        get() = definedExternally
        set(value) = definedExternally
    var renderInteractiveForms: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var continueCallback: ((_continue: () -> Unit) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PDFViewerParams {
    var container: HTMLElement
    var viewer: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PDFRenderTask : PDFLoadingTask<PDFPageProxy> {
    fun cancel()
}

external interface PDFPageProxy {
    var pageIndex: Number
    var pageNumber: Number
    var rotate: Number
    var ref: PDFRef
    var view: Array<Number>
    fun getViewport(params: ViewportParameters): PDFPageViewport
    fun getAnnotations(): PDFPromise<PDFAnnotations>
    fun render(params: PDFRenderParams): PDFRenderTask
    fun getTextContent(): PDFPromise<TextContent>
    fun destroy()
}

external interface TextContentItem {
    var str: String
    var transform: Array<Number>
    var width: Number
    var height: Number
    var dir: String
    var fontName: String
}

external interface TextContent {
    var items: Array<TextContentItem>
    var styles: Any
}

external interface PDFObjects {
    fun get(objId: Number, callback: Any = definedExternally): Any
    fun resolve(objId: Number, data: Any): Any
    fun isResolved(objId: Number): Boolean
    fun hasData(objId: Number): Boolean
    fun getData(objId: Number): Any
    fun clear()
}

external interface PDFJSUtilStatic {
    fun normalizeRect(rect: Array<Number>): Array<Number>
}

external var PDFJS: PDFJSStatic

external interface PDFJSStatic {
    var maxImageSize: Number
    var cMapUrl: String
    var cMapPacked: Boolean
    var disableFontFace: Boolean
    var imageResourcesPath: String
    var disableWorker: Boolean
    var workerSrc: String
    var disableRange: Boolean
    var disableStream: Boolean
    var disableAutoFetch: Boolean
    var pdfBug: Boolean
    var postMessageTransfers: Boolean
    var disableCreateObjectURL: Boolean
    var disableWebGL: Boolean
    var disableFullscreen: Boolean
    var disableTextLayer: Boolean
    var useOnlyCssZoom: Boolean
    var verbosity: Number
    var maxCanvasPixels: Number
    var openExternalLinksInNewWindow: Boolean
    var isEvalSupported: Boolean
    fun PDFViewer(params: PDFViewerParams)
    fun PDFSinglePageViewer(params: PDFViewerParams)
}

external interface PDFLoadingTask<T> {
    var promise: PDFPromise<T>
}

external var Util: PDFJSUtilStatic

external fun getDocument(url: String, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally, progressCallback: (progressData: PDFProgressData) -> Unit = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(url: String): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(url: String, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(url: String, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: Uint8Array, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally, progressCallback: (progressData: PDFProgressData) -> Unit = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: Uint8Array): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: Uint8Array, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: Uint8Array, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBufferView, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally, progressCallback: (progressData: PDFProgressData) -> Unit = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBufferView): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBufferView, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBufferView, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBuffer, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally, progressCallback: (progressData: PDFProgressData) -> Unit = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBuffer): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBuffer, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(data: ArrayBuffer, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(source: PDFSource, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally, progressCallback: (progressData: PDFProgressData) -> Unit = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(source: PDFSource): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(source: PDFSource, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally): PDFLoadingTask<PDFDocumentProxy>

external fun getDocument(source: PDFSource, pdfDataRangeTransport: PDFDataRangeTransport = definedExternally, passwordCallback: (fn: (password: String) -> Unit, reason: String) -> String = definedExternally): PDFLoadingTask<PDFDocumentProxy>