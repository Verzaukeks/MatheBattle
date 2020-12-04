package control.interfaces

import java.util.*

abstract class Tab {

    abstract val id: Int
    abstract var url: String
    abstract var title: String
    abstract var status: String

    abstract fun updateInfo()
    abstract fun reload(bypassCache: Boolean = false)
    abstract fun remove()

    abstract fun executeScript(script: String, async: Boolean = false): String
    abstract fun insertCSS(css: String)

    fun clickElement(selector: String, async: Boolean = false)
        = executeScript("document.querySelector('$selector').click();", async)

    fun inputText(selector: String, value: String, async: Boolean = true)
        = executeScript("document.querySelector('$selector').value = '$value';", async)

    fun querySelector(selector: String = "html")
        = executeScript("document.querySelector('$selector').outerHTML;")

    fun querySelectorAll(selector: String): List<String> {
        val response = executeScript("var ret_38764238074687246823 = ''; document.querySelectorAll('$selector').forEach(element => ret_38764238074687246823 += element.outerHTML + '\\n'); ret_38764238074687246823;")
        if (response.isEmpty()) return ArrayList<String>()
        return response.substring(response.indices).split("\n")
    }

    override fun toString() = "Tab{id=$id, url='$url', title='$title', status=$status}"

}