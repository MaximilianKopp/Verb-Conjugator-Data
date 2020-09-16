package conjugation

import data.Verb
import mode.Indicative
import util.*

class IndicativeImpl(private val verb: Verb) : Indicative {

    override fun present(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(verb.reduceStemTest()) { a, b -> "$a $b" }
        conjugation = conjugation.zip(checkPresentEnding()) { a, b -> "$a$b" }.toMutableList()
        if (verb.reflexivity == "reflexive")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        if (verb.separability == "separable")
            conjugation = conjugation.map { it + " " + verb.prefix }
        return conjugation.joinToString("\n")
    }

    override fun past(): String {
        var conjugation = PERSONAL_PRONOUNS.map { "$it ${verb.reduceStem()}" }
        if (verb.verbclass == "strong")
            conjugation = PERSONAL_PRONOUNS.map { "$it ${verb.past}" }
        conjugation = conjugation.zip(checkPastEnding()) { a, b -> "$a$b" }.toMutableList()
        if (verb.reflexivity == "reflexive")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        if (verb.separability == "separable")
            conjugation = conjugation.map { it + " " + verb.prefix }
        return conjugation.joinToString("\n")
    }

    override fun perfect(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(checkPerfectAuxiliary()) { a, b -> "$a $b" }.toMutableList()
        conjugation = checkProperties(conjugation)
        return conjugation.joinToString("\n")
    }

    override fun pluperfect(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(checkPluPerfectAuxiliary()) { a, b -> "$a $b" }.toMutableList()
        conjugation = checkProperties(conjugation)
        return conjugation.joinToString("\n")
    }

    override fun futureOne(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(AUXILIARY_IND_FUTURE_WERDEN) { a, b -> "$a $b" }.toMutableList()
        if (verb.reflexivity == "reflexive")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        conjugation = conjugation.map { "$it ${verb.infinitive}" }.toMutableList()
        return conjugation.joinToString("\n")
    }

    override fun futureTwo(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(AUXILIARY_IND_FUTURE_WERDEN) { a, b -> "$a $b" }.toMutableList()
        if (verb.reflexivity == "reflexive")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        conjugation = conjugation.map { "$it ${verb.prefix}${verb.pastparticiple} ${verb.auxiliary}" }.toMutableList()
        return conjugation.joinToString("\n")
    }

    private fun checkProperties(part: MutableList<String>): MutableList<String> {
        var conjugation = part
        if (verb.auxiliary != "sein")
            when (verb.reflexivity) {
                "reflexive" -> conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
            }
        when (verb.separability) {
            "separable" -> conjugation = conjugation.map { "$it ${verb.prefix}${verb.pastparticiple}" }.toMutableList()
            "nonSeparable" -> conjugation = conjugation.map { "$it ${verb.pastparticiple}" }.toMutableList()
        }

        return conjugation
    }

    private fun checkPerfectAuxiliary(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.auxiliary) {
                    "haben" -> return AUXILIARY_IND_PRESENT_HABEN
                    "sein" -> return AUXILIARY_IND_PRESENT_SEIN
                }
            }
    }

    private fun checkPluPerfectAuxiliary(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.auxiliary) {
                    "haben" -> return AUXILIARY_IND_PAST_HABEN
                    "sein" -> return AUXILIARY_IND_PAST_SEIN
                }
            }
    }

    private fun checkPresentEnding(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.getStemEnding()) {
                    "s", "ss", "ß", "z", "x", "tz", "chs" -> return PRESENT_STEM_S_SOUND
                }
                when {
                    (verb.getStemEnding() == "d" || verb.getStemEnding() == "t") && verb.stemalternation == "" -> return PRESENT_STEM_TD_SOUND_NOALTER
                    verb.getStemEnding() == "d" && verb.stemalternation != "" -> return PRESENT_STEM_D_SOUND_WITHALTER
                    verb.getStemEnding() == "t" && verb.stemalternation != "" -> return PRESENT_STEM_T_SOUND_WITHALTER
                    ((verb.getStemEnding() == "tm" || verb.getStemEnding() == "ffn" || verb.getStemEnding() == "chn")) -> return PRESENT_STEM_MN_SOUND
                    verb.infinitive.reversed().startsWith("ned") || verb.infinitive.reversed()
                        .startsWith("net") -> return IND_PRESENT_DENTAL
                    verb.infinitive.reversed().startsWith("nen") || verb.infinitive.reversed()
                        .startsWith("nem") -> return IND_PRESENT_NASAL
                }
                when (verb.ending) {
                    "en" -> return IND_PRESENT_EN
                    "eln" -> return IND_PRESENT_ELN
                    "ern" -> return IND_PRESENT_ERN
                    "ieren" -> return IND_PRESENT_IEREN
                    "igen" -> return IND_PRESENT_IGEN
                    "lichen" -> return IND_PRESENT_LICHEN
                    "n" -> return IND_PRESENT_N
                }
            }
    }

    private fun checkPastEnding(): MutableList<String> {
        return mutableListOf<String>()
            .apply {

                if (verb.verbclass == "strong")
                    when (verb.getStemEnding()) {
                        "s", "ss", "ß", "z", "x", "tz", "chs" -> return PAST_STRONG_S_SOUND
                    }

                when (verb.getStemEnding()) {
                    "s", "ss", "ß", "z", "x", "tz", "chs" -> return PAST_WEAK_S_SOUND
                }
                when (verb.verbclass) {
                    "strong" -> return IND_PAST_STRONG
                }

                when (verb.getStemEnding()) {
                    "chn", "d", "ffn", "m", "tm", "t" -> return PAST_TDMN_SOUND
                }

                when (verb.ending) {
                    "en" -> return IND_PAST_EN
                    "ern" -> return IND_PAST_ERN
                    "eln" -> return IND_PAST_ELN
                    "ieren" -> return IND_PAST_IEREN
                    "igen" -> return IND_PAST_IGEN
                    "lichen" -> return IND_PAST_LICHEN
                }
            }
    }
}
