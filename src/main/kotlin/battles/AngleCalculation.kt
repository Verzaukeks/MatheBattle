package battles

import T
import Tl
import control.firefox.FirefoxTab
import battles.utils.VectorPlane
import battles.utils.VectorLine
import kotlin.math.roundToInt

object AngleCalculation {

    private const val lineToLineRadioButton = "#EduBattleEduBattleType36"
    private const val planeToPlaneRadioButton = "#EduBattleEduBattleType34"
    private const val planeToLineRadioButton = "#EduBattleEduBattleType35"

    fun angleCalculation(tab: FirefoxTab, index: Int) {
        when (index % 3) {
            0 -> tab.clickElement(lineToLineRadioButton, true)
            1 -> tab.clickElement(planeToPlaneRadioButton, true)
            2 -> tab.clickElement(planeToLineRadioButton, true)
        }
        tab.clickElement(".submit > input")

        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val g = text.substringAfter("g: ", "").substringBefore(" ").let(::VectorLine)
        val h = text.substringAfter("h: ", "").substringBefore(" ").let(::VectorLine)
        val E = text.substringAfter("E: ", "").let(::areaPrefixCut).substringBefore(" ").let(::VectorPlane)
        val F = text.substringAfter("F: ", "").let(::areaPrefixCut).substringBefore(" ").let(::VectorPlane)

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