package control.firefox

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import control.interfaces.Browser
import control.interfaces.Tab
import control.utils.LineReader
import java.net.ServerSocket
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread

class Firefox : Browser {

    private var server: ServerSocket? = null
    private var thread: Thread? = null

    private var idIndex = 1
    private val gson = Gson()
    private val queue = ArrayList<JsonObject>()
    private val responseQueue = HashMap<Int, JsonObject>()

    private val onTabCreated = ArrayList<(Tab) -> Unit>()
    private val onTabUpdated = ArrayList<(Tab) -> Unit>()

    fun start() {
        if (server != null) return

        server = ServerSocket(58001)
        thread = thread(block = ::run)
    }

    fun stop() {
        if (server == null) return

        thread?.interrupt()
        server?.close()
        queue.clear()

        server = null
        thread = null
    }

    private fun run() {
        while (!Thread.interrupted() && server != null && !server!!.isClosed) {

            val socket = server!!.accept()
            val inputStream = socket.getInputStream()
            val outputStream = socket.getOutputStream()

            val headers = HashMap<String,String>()
            val descriptor = LineReader.readLine(inputStream)
            while (true) {
                val line = LineReader.readLine(inputStream)
                if (line.isEmpty()) break

                val key = line.substringBefore(": ").toLowerCase()
                val value = line.substringAfter(": ")
                headers[key] = value
            }

            val type = descriptor.substringBefore(" ").toUpperCase()
            val contentLength = headers.getOrDefault("content-length", "0").toInt()
            val buffer = ByteArray(contentLength)
            inputStream.read(buffer)

            val json = when (type) {
                "POST" -> {
                    val json = gson.fromJson(buffer.decodeToString(), JsonObject::class.java)
                    runPost(json) ?: JsonObject()
                }
                else -> runGet()
            }.let(gson::toJson)

            var response = ""
            response += "HTTP/1.1 200 OK\n"
            response += "Access-Control-Allow-Origin: *\n"
            response += "Access-Control-Allow-Headers: *\n"
            response += "Content-Type: application/json\n"
            response += "Content-Length: ${json.length}\n"
            response += "\n"
            response += json
            outputStream.write(response.encodeToByteArray())
            outputStream.flush()

            inputStream.close()
            outputStream.close()
            socket.close()
        }
    }

    private fun runGet(): JsonObject {
        return JsonObject().apply {
            add("queue", JsonArray().apply {
                synchronized(queue) {
                    queue.forEach(::add)
                    queue.clear()
                }
            })
        }
    }

    private fun runPost(json: JsonObject): JsonObject? {
        if (json.has("queueId")) {
            val id = json["queueId"].asInt
            responseQueue[id] = json
            return null
        }
        if (json.has("type"))
            when (json["type"].asString) {
                "onTabCreated" -> onTabEvent(json, onTabCreated)
                "onTabUpdated" -> onTabEvent(json, onTabUpdated)
                else -> {}
            }
        return null
    }

    fun request(request: JsonObject): JsonObject {
        val id = idIndex++
        request.addProperty("queueId", id)
        synchronized(queue) { queue += request }

        var tries = 0
        while (true) {

            if (id in responseQueue)
                return responseQueue.remove(id) ?: JsonObject()

            if (++tries >= 12) {
                // TODO - check why it is timing out after some time
                //        /script may be inserted to early
                //        /script may throw error
                //        /querySelector throws null
                // throw TimeoutException("queueId=$id, type=${request.get("type")}")
                return JsonObject()
            }

            Thread.sleep(500)
        }
    }

    override fun newTab(url: String, active: Boolean): Tab {
        val json = request(JsonObject().apply {
            addProperty("type", "newTab")
            addProperty("url", url)
            addProperty("active", active)
        })
        val tab = json.get("tab").asJsonObject

        val id = tab.get("id").asInt
        val title = tab.get("title").asString
        val status = tab.get("status").asString

        return FirefoxTab(this, id, url, title, status)
    }

    override fun getCurrentTab(): FirefoxTab {
        val json = request(JsonObject().apply {
            addProperty("type", "getCurrentTab")
        })
        val tab = json.get("tab").asJsonObject

        val id = tab.get("id").asInt
        val url = tab.get("url").asString
        val title = tab.get("title").asString
        val status = tab.get("status").asString

        return FirefoxTab(this, id, url, title, status)
    }

    override fun getTabs(): List<FirefoxTab> {
        val list = ArrayList<FirefoxTab>()
        val json = request(JsonObject().apply {
            addProperty("type", "getTabs")
        })
        val array = json.get("tabs").asJsonArray

        array.forEach {
            val tab = it.asJsonObject

            val id = tab.get("id").asInt
            val url = tab.get("url").asString
            val title = tab.get("title").asString
            val status = tab.get("status").asString

            list += FirefoxTab(this, id, url, title, status)
        }

        return list
    }

    override fun onTabCreated(block: (Tab) -> Unit) { onTabCreated += block }
    override fun onTabUpdated(block: (Tab) -> Unit) { onTabUpdated += block }

    private fun onTabEvent(json: JsonObject, listener: ArrayList<(Tab) -> Unit>) {
        val tab = json.get("tab").asJsonObject

        val id = tab.get("id").asInt
        val url = tab.get("url").asString
        val title = tab.get("title").asString
        val status = tab.get("status").asString

        val firefoxTab = FirefoxTab(this, id, url, title, status)
        listener.forEach { it(firefoxTab) }
    }

}