package battles

import battles.utils.MathUtil
import battles.utils.Stochastic
import browser.control.Tab
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

object RepetitionOfStochastics {

    private const val aRadioButton = "#EduBattleEduBattleType450"
    private const val bRadioButton = "#EduBattleEduBattleType451"
    private const val cRadioButton = "#EduBattleEduBattleType455"
    private const val dRadioButton = "#EduBattleEduBattleType456"
    private const val eRadioButton = "#EduBattleEduBattleType878"
    private const val fRadioButton = "#EduBattleEduBattleType898"
    private const val gRadioButton = "#EduBattleEduBattleType881"
    private const val hRadioButton = "#EduBattleEduBattleType882"

    // exercises with too many possibilities :/
    fun repetitionOfStochastics(tab: Tab, index: Int) {
        when (index % 8) {
            0 -> tab.clickElement(aRadioButton, false)
            1 -> return // too many possibilities, even diagrams ; tab.clickElement(bRadioButton, false)
            2 -> tab.clickElement(cRadioButton, false)
            3 -> tab.clickElement(dRadioButton, false)
            4 -> tab.clickElement(eRadioButton, false)
            5 -> tab.clickElement(fRadioButton, false)
            6 -> tab.clickElement(gRadioButton, false)
            7 -> tab.clickElement(hRadioButton, false)
        }
        tab.clickElement(".submit > input")

        when (index % 8) {
            0 -> a(tab)
            1 -> b(tab)
            2 -> c(tab)
            3 -> if(d(tab)) return
            4 -> if(e(tab)) return
            5 -> {}
            6 -> {}
            7 -> {}
        }

        tab.clickElement(".submit > input")
    }

    // Pull until x comes for the first time
    private fun a(tab: Tab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', '').replaceAll('\\n', '');")
        val matches = "[0-9]+".toRegex().findAll(text).iterator()
        val sizeSuccess: Int
        val sizeFailure: Int
        val moves: Int

        when {
            "Werder Bremen" in text -> {
                sizeSuccess = 1
                sizeFailure = 3
                moves = matches.next().value.toInt()
            }
            "Kartenstapel" in text -> {
                sizeSuccess = matches.next().value.toInt()
                sizeFailure = matches.next().value.toInt()
                moves = matches.next().value.toInt()
            }
            "Urne" in text -> {
                sizeFailure = matches.next().value.toInt()
                sizeSuccess = matches.next().value.toInt()
                moves = matches.next().value.toInt()
            }
            "Lehrerin" in text -> {
                sizeFailure = matches.next().value.toInt()
                sizeSuccess = matches.next().value.toInt()
                moves = matches.next().value.toInt()
            }
            else -> error("i do not known what to do: $text")
        }

        val (numerator, denominator) = Stochastic.probabilityScFlM(sizeSuccess, sizeFailure, moves)

        tab.inputText(".w350 > div:nth-child(2) > div:nth-child(1) > input:nth-child(1)", "$numerator")
        tab.inputText(".w350 > div:nth-child(2) > div:nth-child(2) > input:nth-child(1)", "$denominator")

        Thread.sleep(1000)
        exitProcess(0)
    }

    // Draw with or without putting back
    private fun b(tab: Tab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', '').replaceAll('\\n', '');")
        val matches = "[0-9]+".toRegex().findAll(text).iterator()
        val numerator: Int
        val denominator: Int

        when {
            "Urne" in text -> {
                /*
                In einer Urne sind 5 Kugeln, die mit einer 1 beschriftet sind, 7 kugel mit einer 2 und 3 Kugeln mit einer 3. Es werden zwei Kugeln gleichzeitig gezogen. Wie groß ist die Wahrscheinlichkeit, dass die Summe der Kugeln 3 ist?
                 */
                val a = matches.next().value.toInt() // 5 Kugeln
                val b = matches.next().value.toInt() // mit 1
                val c = matches.next().value.toInt() // 7 Kugeln
                val d = matches.next().value.toInt() // mit 2
                val e = matches.next().value.toInt() // 3 Kugeln
                val f = matches.next().value.toInt() // mit 3
                val g = 2 // zwei Kugeln gleichzeitig gezogen
                val h = matches.next().value.toInt() // Summe gleich 3

                // TODO make tree object
                val pair = Pair(0, 0)
                numerator = pair.first
                denominator = pair.second
            }
            else -> error("i do not known what to do: $text")
        }

        tab.inputText(".w350 > div:nth-child(2) > div:nth-child(1) > input:nth-child(1)", "$numerator")
        tab.inputText(".w350 > div:nth-child(2) > div:nth-child(2) > input:nth-child(1)", "$denominator")

        Thread.sleep(1000)
        exitProcess(0)
    }

    // Expected value completely open
    private fun c(tab: Tab) {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', '').replaceAll('\\n', '');")
        val matches = "[0-9]+".toRegex().findAll(text).iterator()

        when {
            "Spielautomatenhersteller" in text -> {
                val einsatz = matches.next().value.toDouble()
                val gewinn = matches.next().value.toDouble() / 100.0
                val felder = matches.next().value.toDouble()
                val max = matches.next().value.toDouble()

                val ke = 0.0
                val kp = 0.5
                val ee = max
                val ep = -(ke - einsatz) * kp / (ee - einsatz)

                val ze = einsatz - (einsatz - ke) / 2
                val be = einsatz + (einsatz - ke) / 2
                val zp = -(0.05 + gewinn) / (ze - einsatz)
                val bp =  (0.05         ) / (be - einsatz)

                val ae = einsatz
                val ap = 1 - (kp + zp + bp + ep)

                val kirscheEuro = "$ke"
                val kirscheP = "$kp"
                val zitroneEuro = "$ze"
                val zitroneP = "$zp"
                val apfelEuro = "$ae"
                val apfelP = "$ap"
                val bananeEuro = "$be"
                val bananeP = "$bp"
                val erdbeereEuro = "$ee"
                val erdbeereP = "$ep"
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > input:nth-child(1)", kirscheEuro)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > input:nth-child(1)", kirscheP)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3) > input:nth-child(1)", zitroneEuro)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(3) > input:nth-child(1)", zitroneP)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4) > input:nth-child(1)", apfelEuro)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(4) > input:nth-child(1)", apfelP)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(5) > input:nth-child(1)", bananeEuro)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(5) > input:nth-child(1)", bananeP)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(6) > input:nth-child(1)", erdbeereEuro)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(6) > input:nth-child(1)", erdbeereP)
            }
            "Schulfest" in text -> {
                val beispiel = matches.next().value.toDouble()
                val einsatz = matches.next().value.toDouble()
                val min = matches.next().value.toDouble()
                val max = matches.next().value.toDouble()

                val f1a = min
                val f1p = 0.5
                val f4a = max
                val f4p = -(f1a - einsatz) * f1p / (f4a - einsatz)

                val f2a = einsatz - (einsatz - f1a) / 2
                val f3a = einsatz + (einsatz - f1a) / 2
                val f2p = (1 - (f1p + f4p)) / 2
                val f3p = f2p

                val f1Auszahlung = "$f1a"
                val f1P = "$f1p"
                val f1Winkel = "${f1p * 360}"
                val f2Auszahlung = "$f2a"
                val f2P = "$f2p"
                val f2Winkel = "${f2p * 360}"
                val f3Auszahlung = "$f3a"
                val f3P = "$f3p"
                val f3Winkel = "${f3p * 360}"
                val f4Auszahlung = "$f4a"
                val f4P = "$f4p"
                val f4Winkel = "${f4p * 360}"
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > input:nth-child(1)", f1Auszahlung)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > input:nth-child(1)", f1P)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2) > input:nth-child(1)", f1Winkel)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3) > input:nth-child(1)", f2Auszahlung)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(3) > input:nth-child(1)", f2P)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(3) > input:nth-child(1)", f2Winkel)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4) > input:nth-child(1)", f3Auszahlung)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(4) > input:nth-child(1)", f3P)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(4) > input:nth-child(1)", f3Winkel)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(5) > input:nth-child(1)", f4Auszahlung)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(5) > input:nth-child(1)", f4P)
                tab.inputText(".e_wert_table > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(5) > input:nth-child(1)", f4Winkel)
            }
            else -> error("i do not known what to do: $text")
        }

        Thread.sleep(1000)
        exitProcess(0)
    }

    // Expectation values
    private fun d(tab: Tab): Boolean {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', '').replaceAll('\\n', '');")
        val matches = "[0-9]+".toRegex().findAll(text).iterator()

        when {
            "Ein Spieler darf einmal Würfeln." in text -> {
                val a = matches.next().value.toInt()
                val aEuro = matches.next().value.toDouble()
                val b = matches.next().value.toInt()
                val bEuro = matches.next().value.toDouble()
                val c = matches.next().value.toInt()
                val cEuro = matches.next().value.toDouble()
                val _1 = matches.next().value.toInt()
                val _2 = matches.next().value.toInt()
                val _3 = matches.next().value.toInt()
                val rEuro = matches.next().value.toDouble()

                var euro = 0.0
                euro += aEuro / 6
                euro += bEuro / 6
                euro += cEuro / 6
                euro += rEuro / 2

                tab.inputText(".value_form > input:nth-child(1)", "$euro")
            }
            "Urne" in text && "durchschnittlich" in text -> {
                val blue = matches.next().value.toInt()
                val red = matches.next().value.toInt()
                val green = matches.next().value.toInt()
                val white = matches.next().value.toInt()
                val blueEuro = matches.next().value.toDouble()
                val redEuro = matches.next().value.toDouble()
                val greenEuro = matches.next().value.toDouble()
                val whiteEuro = matches.next().value.toDouble()

                val size = blue + red + green + white
                var euro = 0.0
                euro += blueEuro * blue / size
                euro += redEuro * red / size
                euro += greenEuro * green / size
                euro += whiteEuro * white / size

                tab.inputText(".value_form > input:nth-child(1)", "$euro")
            }
            "In einer Urne sind" in text -> {
                val aS = matches.next().value.toDouble()
                val aE = matches.next().value.toDouble()
                val bS = matches.next().value.toDouble()
                val bE = matches.next().value.toDouble()
                val cS = matches.next().value.toDouble()
                val cE = matches.next().value.toDouble()
                val rS = matches.next().value.toDouble()
                val einsatz = "[0-9]+,[0-9]+".toRegex().findAll(text).iterator().next().value.replace(',', '.').toDouble()

                val size = aS + bS + cS + rS
                var euro = 0.0
                euro += aE * aS / size
                euro += bE * bS / size
                euro += cE * cS / size
                val rE = (einsatz - euro) * size / rS

                tab.inputText(".value_form > input:nth-child(1)", "$rE")
            }
            "Ein Spieler darf aus einer Urne" in text -> {
                val blue = matches.next().value.toInt()
                val red = matches.next().value.toInt()
                val green = matches.next().value.toInt()
                val white = matches.next().value.toInt()
                val blueEuro = matches.next().value.toDouble()
                val redEuro = matches.next().value.toDouble()
                val greenEuro = matches.next().value.toDouble()
                val einsatz = matches.next().value.toDouble()


                val size = blue + red + green + white
                var euro = 0.0
                euro += blueEuro * blue / size
                euro += redEuro * red / size
                euro += greenEuro * green / size
                val whiteEuro = (einsatz - euro) * size / white

                tab.inputText(".value_form > input:nth-child(1)", "$whiteEuro")
            }
            "Glücksrad" in text -> {
                // hehe, i am not willing to parse an image
                tab.executeScript("window.history.back()", true)
                return true
            }
            "Tombola" in text -> {
                val on10 = matches.next().value.toInt()
                val on5 = matches.next().value.toInt()
                val on4 = matches.next().value.toInt()
                val onOther = matches.next().value.toInt()

                var dp = 0.0
                dp += on10 / 10.0
                dp += on5 / 5.0
                dp += on4 / 4.0
                dp += onOther / 7.0

                tab.inputText(".value_form > input:nth-child(1)", "$dp")
            }
            else -> error("i do not known what to do: $text")
        }

        Thread.sleep(1000)
        exitProcess(0)
        return false
    }

    // VFT Application Frequencies
    private fun e(tab: Tab): Boolean {
        val text = tab.executeScript("document.querySelector('.exercise_question').textContent.replaceAll('\\t', '').replaceAll('\\n', '');")
        val matches = "[0-9]+".toRegex().findAll(text).iterator()

        when {
            "Monat" in text && ", an denen keine Schule war." in text -> {
                val all = matches.next().value.toInt()
                val ns = matches.next().value.toInt()
                val sw = matches.next().value.toInt()
                val nsnw = matches.next().value.toInt()

                val nsw = ns - nsnw

                tab.inputText(".value_form > input:nth-child(1)", "$nsw")
            }
            "Monat" in text && "Tage mit schönem Wetter." in text -> {
                val all = matches.next().value.toInt()
                val w = matches.next().value.toInt()
                val sw = matches.next().value.toInt()
                val nsnw = matches.next().value.toInt()

                val ns = w - sw + nsnw

                tab.inputText(".value_form > input:nth-child(1)", "$ns")
            }
            "Gymnasiums" in text -> {
                tab.executeScript("window.history.back()", true)
                return true
            }
            "Fahrradhändler" in text -> {
                val all = matches.next().value.toInt()
                val mne = matches.next().value.toInt()
                val e = matches.next().value.toInt()
                val nm = matches.next().value.toInt()

                val nmne = all - e - mne

                tab.inputText(".value_form > input:nth-child(1)", "$nmne")
            }
            "Jahrgangstufe" in text && "Wieviel Mädchen sind in der Klasse?" in text -> {
                matches.next().value.toInt()
                val all = matches.next().value.toInt()
                val ml = matches.next().value.toInt()
                val jnl = matches.next().value.toInt()
                val nl = matches.next().value.toInt()

                val m = nl - jnl + ml

                tab.inputText(".value_form > input:nth-child(1)", "$m")
            }
            "Jahrgangstufe" in text -> {
                matches.next().value.toInt()
                val ml = matches.next().value.toInt()
                val jnl = matches.next().value.toInt()
                val nl = matches.next().value.toInt()
                val j = matches.next().value.toInt()

                val all = nl - jnl + ml + j

                tab.inputText(".value_form > input:nth-child(1)", "$all")
            }
            else -> error("i do not known what to do: $text")
        }

        Thread.sleep(1000)
        exitProcess(0)
        return false
    }

}