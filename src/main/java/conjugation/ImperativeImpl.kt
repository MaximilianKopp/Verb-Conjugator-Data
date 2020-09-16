package conjugation

import data.Verb
import mode.Imperative
import util.*

class ImperativeImpl(private var verb: Verb) : Imperative {

    override fun imp(): String {
        var conjugation = PERSONAL_PRONOUNS.map { verb.reduceStem() }.toMutableList()
        conjugation = conjugation.zip(checkImperativeEnding()) { a, b -> "$a$b" }.toMutableList()
        conjugation = conjugation.zip(PERSONAL_PRONOUNS_IMPERATIVE) { a, b -> "$a $b" }.toMutableList()
        conjugation = checkProperties(conjugation)

        return conjugation.joinToString("\n")
    }

    private fun checkProperties(part: MutableList<String>): MutableList<String> {
        var conjugation = part
        if (verb.auxiliary != "sein")
            when (verb.reflexivity) {
                "reflexive" -> conjugation =
                    conjugation.zip(REFLEXIVE_PRONOUNS_IMPERATIVE) { a, b -> "$a $b" }.toMutableList()
            }
        when (verb.separability) {
            "separable" -> conjugation = conjugation.map { "$it ${verb.prefix}" }.toMutableList()
            "nonSeparable" -> conjugation = conjugation.map { it }.toMutableList()
        }

        return conjugation
    }

    private fun checkImperativeEnding(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                when {
                    verb.infinitive.reversed().startsWith("ned") || verb.infinitive.reversed().startsWith("net") -> return IMPERATIVE_DENTAL
                    verb.infinitive.reversed().startsWith("nen") || verb.infinitive.reversed().startsWith("nem") -> return IMPERATIVE_NASAL
                }
                when (verb.ending) {
                    "en" -> return IMPERATIVE_EN
                    "eln" -> return IMPERATIVE_ELN
                    "ern" -> return IMPERATIVE_ERN
                    "ieren" -> return IMPERATIVE_IEREN
                    "igen" -> return IMPERATIVE_IGEN
                    "lichen" -> return IMPERATIVE_LICHEN
                }
            }
    }
}