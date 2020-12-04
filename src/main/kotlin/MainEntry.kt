import battles.AngleCalculation.angleCalculation
import battles.AreaVolumeCalculation.areaVolumeCalculation
import battles.RepetitionOfAnalysis.repetitionOfAnalysis
import control.firefox.Firefox

// https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API
// about:debugging#/runtime/this-firefox

const val T = "\\t"
const val Tl = T.length

fun main(args: Array<String>) {

    val firefox = Firefox()
    firefox.start()

    var index = 0
    val tab = firefox.getTabs().first { it.url.startsWith("https://mathebattle.de") }
    while (true) {
        //angleCalculation(tab, index++)
        //areaVolumeCalculation(tab, index++)
        repetitionOfAnalysis(tab, index++)
    }

}