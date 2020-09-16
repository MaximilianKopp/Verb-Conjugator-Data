package conjugation

import data.Verb
import mode.Subjunctive
import util.*

class SubjunctiveImpl(private var verb: Verb) : Subjunctive {

    override fun one(): String {
        var conjugation = PERSONAL_PRONOUNS.map { "$it ${verb.reduceStem()}" }
        conjugation = conjugation.zip(checkSubjOneEnding()) { a, b -> "$a$b" }.toMutableList()
        if (verb.reflexivity == "reflexive" && verb.auxiliary != "sein")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        if (verb.separability == "separable")
            conjugation = conjugation.map { it + " " + verb.prefix }
        return conjugation.joinToString("\n")
    }

    override fun two(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(AUXILIAR_SUBJ_TWO_WERDEN) { a, b -> "$a $b" }.toMutableList()
        if (verb.reflexivity == "reflexive" && verb.auxiliary != "sein")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        conjugation = conjugation.map { "$it ${verb.infinitive}" }.toMutableList()
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
        var conjugation = PERSONAL_PRONOUNS.zip(AUXILIAR_SUBJ_ONE_WERDEN) { a, b -> "$a $b" }.toMutableList()
        if (verb.reflexivity == "reflexive" && verb.auxiliary != "sein")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        conjugation = conjugation.map { "$it ${verb.infinitive}" }.toMutableList()
        return conjugation.joinToString("\n")
    }

    override fun futureTwo(): String {
        var conjugation = PERSONAL_PRONOUNS.zip(AUXILIAR_SUBJ_ONE_WERDEN) { a, b -> "$a $b" }.toMutableList()
        if (verb.reflexivity == "reflexive" && verb.auxiliary != "sein")
            conjugation = conjugation.zip(REFLEXIVE_PRONOUNS) { a, b -> "$a $b" }.toMutableList()
        conjugation = conjugation.map { "$it ${verb.prefix}${verb.pastparticiple} ${verb.auxiliary}" }.toMutableList()
        return conjugation.joinToString("\n")
    }

    private fun checkPerfectAuxiliary(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.auxiliary) {
                    "haben" -> return AUXILIAR_SUBJ_ONE_HABEN
                    "sein" -> return AUXILIAR_SUBJ_ONE_SEIN
                }
            }
    }

    private fun checkPluPerfectAuxiliary(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.auxiliary) {
                    "haben" -> return AUXILIAR_SUBJ_TWO_HABEN
                    "sein" -> return AUXILIAR_SUBJ_TWO_SEIN
                }
            }
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

    private fun checkSubjOneEnding(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when (verb.getStemEnding()) {
                    "s", "ss", "ß", "z", "x", "tz", "chs" -> return PRESENT_STEM_S_SOUND
                }
                when {
                    (verb.getStemEnding() == "d" || verb.getStemEnding() == "t") && verb.stemalternation == "" -> return PRESENT_STEM_TD_SOUND_NOALTER
                    verb.getStemEnding() == "d" && verb.stemalternation != "" -> return PRESENT_STEM_D_SOUND_WITHALTER
                    verb.getStemEnding() == "t" && verb.stemalternation != "" -> return PRESENT_STEM_T_SOUND_WITHALTER
                    verb.getStemEnding() == "tm" || verb.getStemEnding() == "ffn" || verb.getStemEnding() == "chn" -> return PRESENT_STEM_MN_SOUND
                    verb.infinitive.reversed().startsWith("ned") || verb.infinitive.reversed().startsWith("net") -> return SUBJ_ONE_DENTAL
                    verb.infinitive.reversed().startsWith("nen") || verb.infinitive.reversed().startsWith("nem") -> return SUBJ_ONE_NASAL
                }
                when (verb.ending) {
                    "en" -> return SUBJ_ONE_PRESENT_EN
                    "eln" -> return SUBJ_ONE_PRESENT_ELN
                    "ern" -> return SUBJ_ONE_PRESENT_ERN
                    "ieren" -> return SUBJ_ONE_PRESENT_IEREN
                    "igen" -> return SUBJ_ONE_PRESENT_IGEN
                    "lichen" -> return SUBJ_ONE_PRESENT_LICHEN
                }
            }
    }
}