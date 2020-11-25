package control.firefox

import com.google.gson.JsonObject
import control.interfaces.Tab

class FirefoxTab(
    private val firefox: Firefox,
    override val id: Int,
    override var url: String,
    override var title: String,
    override var status: String
) : Tab() {

    override fun updateInfo() {
        val json = firefox.request(JsonObject().apply {
            addProperty("type", "getTab")
            addProperty("id", id)
        })
        val tab = json.get("tab").asJsonObject

        url = tab.get("url").asString
        title = tab.get("title").asString
        status = tab.get("status").asString
    }

    override fun reload(bypassCache: Boolean) {
        firefox.request(JsonObject().apply {
            addProperty("type", "reloadTab")
            addProperty("id", id)
            addProperty("bypassCache", bypassCache)
        })
    }

    override fun remove() {
        firefox.request(JsonObject().apply {
            addProperty("type", "removeTab")
            addProperty("id", id)
        })
    }

    override fun executeScript(script: String): String {
        val json = firefox.request(JsonObject().apply {
            addProperty("type", "executeScript")
            addProperty("id", id)
            addProperty("script", script)
        })
        return json.get("result").asString
    }

    override fun insertCSS(css: String) {
        firefox.request(JsonObject().apply {
            addProperty("type", "insertCSS")
            addProperty("id", id)
            addProperty("css", css)
        })
    }

}