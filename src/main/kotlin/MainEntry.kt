import battles.RepetitionOfAnalysis.repetitionOfAnalysis
import control.Browser

// https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API
// about:debugging#/runtime/this-firefox

const val T = "\\t"
const val Tl = T.length

fun main(args: Array<String>) {

    val browser = Browser()
    browser.start()

    var index = 0
    val tab = browser.getTabs().first { it.url.startsWith("https://mathebattle.de") }
    while (true) {
        //angleCalculation(tab, index++)
        //areaVolumeCalculation(tab, index++)
        repetitionOfAnalysis(tab, index++)
    }

}