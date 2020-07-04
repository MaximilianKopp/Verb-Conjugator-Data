package conjugation

import data.Verb
import mode.Participle

class ParticipleImpl(private var verb: Verb) : Participle {

    override fun one(): String {
        return "${verb.prefix}${verb.reduceStem()}${setEnding()}"
    }

    override fun two(): String {
        return "${verb.prefix}${verb.pastparticiple}"
    }

    private fun setEnding(): String {
        when (verb.ending) {
            "en" -> return "end"
            "ern" -> return "ernd"
            "eln" -> return "elnd"
            "ieren" -> return "ierend"
            "igen" -> return "igend"
            "lichen" -> return "lichend"
        }
        return "en"
    }
}
