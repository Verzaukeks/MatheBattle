import control.firefox.Firefox
import control.firefox.FirefoxTab
import control.utils.Ebene
import control.utils.Gerade
import kotlin.math.absoluteValue
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.roundToInt

// https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API
// about:debugging#/runtime/this-firefox

const val T = "\\t"
const val Tl = T.length
const val TIME_TO_SLEEP = 300L

fun main(args: Array<String>) {

    val firefox = Firefox()
    firefox.start()

    var index = 0
    val tab = firefox.getTabs().first { it.url.startsWith("https://mathebattle.de") }
    while (true) {
        winkelberechnungen(tab, index++)
    }

}

fun winkelberechnungen(tab: FirefoxTab, index: Int) {
    val i = index % 3
    if (i == 0) winkelberechnungen_gerade_gerade(tab)
    if (i == 1) winkelberechnungen_ebene_ebene(tab)
    if (i == 2) winkelberechnungen_ebene_gerade(tab)
}
fun winkelberechnungen_gerade_gerade(tab: FirefoxTab) {
    tab.clickElement("#EduBattleEduBattleType36")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)

    val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', 'T_').replaceAll('\\n', '');").let(::cutTab)
    val g = text.substringAfter("g: ").substringBefore(" ").let { Gerade(it) }
    val h = text.substringAfter("h: ").substringBefore(" ").let { Gerade(it) }

    val counter = g.u.product(h.u).absoluteValue
    val denominator = g.u.length() * h.u.length()
    val rad = acos(counter / denominator)
    val alpha = Math.toDegrees(rad)
    val rounded = (alpha * 10).roundToInt() / 10.0

    tab.inputText(".value_form > input", "$rounded")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)
}
fun winkelberechnungen_ebene_ebene(tab: FirefoxTab) {
    tab.clickElement("#EduBattleEduBattleType34")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)

    val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', 'T_').replaceAll('\\n', '');").let(::cutTab)
    val E = text.substringAfter("E: ").let(Ebene::extraCut).substringBefore(" ").let { Ebene(it) }
    val F = text.substringAfter("F: ").let(Ebene::extraCut).substringBefore(" ").let { Ebene(it) }

    val counter = E.n.product(F.n).absoluteValue
    val denominator = E.n.length() * F.n.length()
    val rad = acos(counter / denominator)
    val alpha = Math.toDegrees(rad)
    val rounded = (alpha * 10).roundToInt() / 10.0

    tab.inputText(".value_form > input", "$rounded")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)
}
fun winkelberechnungen_ebene_gerade(tab: FirefoxTab) {
    tab.clickElement("#EduBattleEduBattleType35")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)

    val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', 'T_').replaceAll('\\n', '');").let(::cutTab)
    val E = text.substringAfter("E: ").let(Ebene::extraCut).substringBefore(" ").let { Ebene(it) }
    val g = text.substringAfter("g: ").substringBefore(" ").let { Gerade(it) }

    val counter = E.n.product(g.u).absoluteValue
    val denominator = E.n.length() * g.u.length()
    val rad = asin(counter / denominator)
    val alpha = Math.toDegrees(rad)
    val rounded = (alpha * 10).roundToInt() / 10.0

    tab.inputText(".value_form > input", "$rounded")
    tab.clickElement(".submit > input")
    Thread.sleep(TIME_TO_SLEEP)
}
fun cutTab(raw: String): String {
    var res = raw
    while (true) {
        val index = res.indexOf("T_T_")
        if (index == -1) return res.replace("T_", T)
        res = res.replace("T_T_", "T_")
    }
}