package battles

import battles.utils.genral.Point
import browser.control.Tab
import kotlin.math.absoluteValue

object AreaVolumeCalculation {

    private const val areaParallelogramRadioButton = "#EduBattleEduBattleType932"
    private const val areaTriangleRadioButton = "#EduBattleEduBattleType931"
    private const val volumePyramidRadioButton = "#EduBattleEduBattleType82"
    private const val volumeTrianglePyramidRadioButton = "#EduBattleEduBattleType933"

    fun areaVolumeCalculation(tab: Tab, index: Int) {
        when (index % 4) {
            0 -> tab.clickElement(areaParallelogramRadioButton, false)
            1 -> tab.clickElement(areaTriangleRadioButton, false)
            2 -> tab.clickElement(volumePyramidRadioButton, false)
            3 -> tab.clickElement(volumeTrianglePyramidRadioButton, false)
        }
        tab.clickElement(".submit > input")

        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '').replaceAll('\\t', '');")
        val A = text.substringAfter("A(", "").substringBefore(")").let(::Point)
        val B = text.substringAfter("B(", "").substringBefore(")").let(::Point)
        val C = text.substringAfter("C(", "").substringBefore(")").let(::Point)
        val D = text.substringAfter("D(", "").substringBefore(")").let(::Point)
        val S = text.substringAfter("S(", "").substringBefore(")").let(::Point)

        when (index % 4) {
            0 -> areaParallelogram(tab, A, B, C)
            1 -> areaTriangle(tab, A, B, C)
            2 -> volumePyramid(tab, A, B, D, S)
            3 -> volumeTrianglePyramid(tab, A, B, C, S)
        }

        tab.clickElement(".submit > input")
    }

    private fun areaParallelogram(tab: Tab, A: Point, B: Point, C: Point) {
        val BC = B.vectorTo(C)
        val D = A + BC

        val AB = A.vectorTo(B)
        val AD = A.vectorTo(D)
        val area = AB.crossProduct(AD).length()

        tab.inputText(".point_form > input:nth-child(1)", "${D.a.toInt()}")
        tab.inputText(".point_form > input:nth-child(2)", "${D.b.toInt()}")
        tab.inputText(".point_form > input:nth-child(3)", "${D.c.toInt()}")
        tab.inputText(".value_form > input", "${area.toInt()}")
    }

    private fun areaTriangle(tab: Tab, A: Point, B: Point, C: Point) {
        val AB = A.vectorTo(B)
        val AC = A.vectorTo(C)
        val area = AB.crossProduct(AC).length() * 0.5

        tab.inputText(".value_form > input", "${area.toInt()}")
    }

    private fun volumePyramid(tab: Tab, A: Point, B: Point, D: Point, S: Point) {
        val AB = A.vectorTo(B)
        val AD = A.vectorTo(D)
        val AS = A.vectorTo(S)
        val volume = AB.crossProduct(AD).scalarProduct(AS).absoluteValue / 3

        tab.inputText(".value_form > input", "${volume.toInt()}")
    }

    private fun volumeTrianglePyramid(tab: Tab, A: Point, B: Point, C: Point, S: Point) {
        val AB = A.vectorTo(B)
        val AC = A.vectorTo(C)
        val AS = A.vectorTo(S)
        val volume = AB.crossProduct(AC).scalarProduct(AS).absoluteValue / 6

        tab.inputText(".value_form > input", "${volume.toInt()}")
    }

}