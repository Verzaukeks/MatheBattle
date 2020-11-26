package battles

import T
import TIME_TO_SLEEP
import Tl
import control.firefox.FirefoxTab
import battles.utils.VectorArea
import battles.utils.VectorLine
import kotlin.math.absoluteValue
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.roundToInt

object AngleCalculation {

    private const val lineToLineRadioButton = "#EduBattleEduBattleType36"
    private const val areaToAreaRadioButton = "#EduBattleEduBattleType34"
    private const val areaToLineRadioButton = "#EduBattleEduBattleType35"

    fun angleCalculation(tab: FirefoxTab, index: Int) {
        when (index % 3) {
            0 -> tab.clickElement(lineToLineRadioButton)
            1 -> tab.clickElement(areaToAreaRadioButton)
            2 -> tab.clickElement(areaToLineRadioButton)
        }
        tab.clickElement(".submit > input")
        Thread.sleep(TIME_TO_SLEEP)

        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val g = text.substringAfter("g: ", "").substringBefore(" ").let(::VectorLine)
        val h = text.substringAfter("h: ", "").substringBefore(" ").let(::VectorLine)
        val E = text.substringAfter("E: ", "").let(::areaPrefixCut).substringBefore(" ").let(::VectorArea)
        val F = text.substringAfter("F: ", "").let(::areaPrefixCut).substringBefore(" ").let(::VectorArea)

        var angle = when (index % 3) {
            0 -> g.u.angleWith(h.u)
            1 -> E.n.angleWith(F.n)
            2 -> 90 - E.n.angleWith(g.u)
            else -> Double.NaN
        }
        if (angle.isNaN()) angle = 0.0
        angle = (angle * 10).roundToInt() / 10.0

        tab.inputText(".value_form > input", "$angle")
        tab.clickElement(".submit > input")
        Thread.sleep(TIME_TO_SLEEP)
    }

    private fun cutTab(raw: String): String {
        var res = raw
        while (true) {
            val index = res.indexOf("\t\t")
            if (index == -1) return res.replace("\t", T)
            res = res.replace("\t\t", "\t")
        }
    }

    private fun areaPrefixCut(ebene: String): String {
        if (ebene.startsWith("$T ")) return ebene.substring(Tl+1)
        return ebene
    }

}