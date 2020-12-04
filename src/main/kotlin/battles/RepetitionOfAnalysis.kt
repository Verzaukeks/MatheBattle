package battles

import T
import TIME_TO_SLEEP
import battles.utils.EulerFunction
import battles.utils.Interval
import battles.utils.PolynomialFunction
import battles.utils.TrigonometricFunction
import control.firefox.FirefoxTab
import kotlin.math.roundToInt

object RepetitionOfAnalysis {

    private const val turningPointsRadioButton = "#EduBattleEduBattleType719"
    private const val inflectionPointsRadioButton = "#EduBattleEduBattleType720"
    private const val intervalMonotonicityRadioButton = "#EduBattleEduBattleType400"
    private const val intervalCurvatureBehaviorRadioButton = "#EduBattleEduBattleType401"
    private const val inflectionTangentsRadioButton = "#EduBattleEduBattleType721"
    private const val turningPointsTrigonometricRadioButton = "#EduBattleEduBattleType723"
    private const val turningPointsERadioButton = "#EduBattleEduBattleType424"

    fun repetitionOfAnalysis(tab: FirefoxTab, index: Int) {
        when (index % 7) {
            0 -> tab.clickElement(turningPointsRadioButton)
            1 -> tab.clickElement(inflectionPointsRadioButton)
            2 -> tab.clickElement(intervalMonotonicityRadioButton)
            3 -> tab.clickElement(intervalCurvatureBehaviorRadioButton)
            4 -> tab.clickElement(inflectionTangentsRadioButton)
            5 -> tab.clickElement(turningPointsTrigonometricRadioButton)
            6 -> tab.clickElement(turningPointsERadioButton)
        }
        tab.clickElement(".submit > input")
        Thread.sleep(TIME_TO_SLEEP)

        when (index % 7) {
            0 -> turningPoints(tab)
            1 -> inflectionPoints(tab)
            2 -> intervalMonotonicity(tab)
            3 -> intervalCurvatureBehavior(tab)
            4 -> inflectionTangents(tab)
            5 -> turningPointsTrigonometric(tab)
            6 -> turningPointsE(tab)
        }

        tab.clickElement(".submit > input")
        Thread.sleep(TIME_TO_SLEEP)
    }

    private fun turningPoints(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val type = if ("·" in text) 2 else 1

        val f = PolynomialFunction(text, type)
        val df = f.differentiate()
        val ddf = df.differentiate()

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")

        var index = 0
        val yz = df.replaceYwith0().sorted()
        for (x in yz) {

            val y = f.replaceX(x)
            val ddy = ddf.replaceX(x)
            if (ddy == 0.0) continue

            tab.executeScript("document.querySelector('#opt_$index').children[0].children[${if (ddy < 0) 0 else 1}].selected=true;")
            tab.inputText("#x$index", "${x.toInt()}")
            tab.inputText("#y$index", "${y.toInt()}")

            index ++
            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    // sometimes wrong?
    private fun inflectionPoints(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val type = if ("·" in text) 2 else 1

        val f = PolynomialFunction(text, type)
        val df = f.differentiate()
        val ddf = df.differentiate()
        val dddf = ddf.differentiate()

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")

        var index = 0
        val yz = ddf.replaceYwith0().sorted()
        for (x in yz) {

            val y = f.replaceX(x)
            val dddy = dddf.replaceX(x)
            if (dddy == 0.0) continue

            tab.inputText("#x$index", "${x.toInt()}")
            tab.inputText("#y$index", "${y.toInt()}")

            index ++
            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    // sometimes wrong?
    private fun intervalMonotonicity(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val type = 3

        val f = PolynomialFunction(text, type)
        val df = f.differentiate()
        val ddf = df.differentiate()

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")
        for (index in 0..4) {
            tab.inputText("#x$index", "")
            tab.inputText("#y$index", "")
        }

        val yz = df.replaceYwith0().apply {
            add(Double.POSITIVE_INFINITY)
        }.sorted()
        for ((index, x) in yz.withIndex()) {

            // ∞ = U
            val end = x == Double.POSITIVE_INFINITY
            val prev = if (index > 0) yz[index - 1].toInt().toString() else "-U"
            val cur = if (end) "U" else x.toInt().toString()
            val ddy = ddf.replaceX(if (end && index > 0) yz[index - 1] else x) * if (end) -1 else 1

            tab.executeScript("document.querySelector('#opt_$index').children[0].children[${if (ddy < 0) 0 else 1}].selected=true;")
            tab.inputText("#x$index", prev)
            tab.inputText("#y$index", cur)

            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    private fun intervalCurvatureBehavior(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val type = 3

        val f = PolynomialFunction(text, type)
        val df = f.differentiate()
        val ddf = df.differentiate()

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")
        for (index in 0..4) {
            tab.inputText("#x$index", "")
            tab.inputText("#y$index", "")
        }

        val yz = ddf.replaceYwith0().apply {
            add(Double.POSITIVE_INFINITY)
        }.sorted()
        for ((index, x) in yz.withIndex()) {

            // ∞ = U
            val end = x == Double.POSITIVE_INFINITY
            val prev = if (index > 0) yz[index - 1].toInt().toString() else "-U"
            val cur = if (end) "U" else x.toInt().toString()
            val ddx = if (end) { yz[index - 1] + 1 } else { x - 1 }
            val ddy = ddf.replaceX(ddx)

            tab.executeScript("document.querySelector('#opt_$index').children[0].children[${if (ddy > 0) 0 else 1}].selected=true;")
            tab.inputText("#x$index", prev)
            tab.inputText("#y$index", cur)

            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    private fun inflectionTangents(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val type = 3

        val f = PolynomialFunction(text, type)
        val df = f.differentiate()
        val ddf = df.differentiate()
        val dddf = ddf.differentiate()

        val yz = ddf.replaceYwith0().sorted()
        for (x in yz) {

            val dddy = dddf.replaceX(x)
            if (dddy == 0.0) continue

            val y = f.replaceX(x)
            val m = df.replaceX(x)
            val c = y - (m * x)

            tab.inputText("#term_input", "${m.toInt()}x+(${c.toInt()})")
        }
    }

    private fun turningPointsTrigonometric(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)

        val asked = when {
            "Hochpunkte" in text -> 1
            "Tiefpunkte" in text -> 2
            "Wendepunkte" in text -> 3
            else -> error("cannot interpret")
        }
        val f = TrigonometricFunction(text)
        val iv = Interval(text)

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")

        var maxi: Double
        var mini: Double
        var infl: Double
        var period = 2 * Math.PI / f.b
        if (f.cos) {
            maxi = 0.0
            mini = period / 2
            infl = period / 4
        } else {
            maxi = period / 4
            mini = period * 3 / 4
            infl = 0.0
        }

        while (maxi > iv.a || mini > iv.a || infl > iv.a) {
            maxi -= period
            mini -= period
            infl -= period
        }
        var toCalc = when(asked) {
            1 -> if (f.a >= 0) maxi else mini
            2 -> if (f.a >= 0) mini else maxi
            3 -> infl
            else -> error("???")
        }
        if (asked == 3) period /= 2.0

        var index = 0
        while (toCalc < iv.b) {

            val x = toCalc
            val y = f.replaceX(x)

            toCalc += period
            if (x < iv.a)
                continue

            tab.inputText("#x$index", "$x".replace(".", ","))
            tab.inputText("#y$index", "${y.roundToInt()}")

            index ++
            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    private fun turningPointsE(tab: FirefoxTab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\n', '');").let(::cutTab)
        val raw = text.substringAfter("f(x)=$T").substringBefore("$T:$T")
        val type = raw.count { it == 'e' }

        val f = EulerFunction(raw, type)
        val df = f.differentiate()
        val ddf = df.differentiate()

        tab.executeScript("document.querySelector('#anzahl').children[0].selected=true;")

        var index = 0
        val yz = df.replaceYwith0().sorted()
        for (x in yz) {

            val y = f.replaceX(x)
            val ddy = ddf.replaceX(x)
            if (ddy == 0.0) continue

            val dx = "${(x * 1000000).roundToInt() / 1000000.0}".replace(".", ",")
            val dy = "${(y * 1000000).roundToInt() / 1000000.0}".replace(".", ",")

            tab.executeScript("document.querySelector('#opt_$index').children[0].children[${if (ddy < 0) 0 else 1}].selected=true;")
            tab.inputText("#x$index", dx)
            tab.inputText("#y$index", dy)

            index ++
            tab.executeScript("document.querySelector('#anzahl').children[$index].selected=true;")
        }
    }

    private fun cutTab(raw: String): String {
        var res = raw
        while (true) {
            val index = res.indexOf("\t\t")
            if (index == -1) return res.replace("\t", T)
            res = res.replace("\t\t", "\t")
        }
    }

}