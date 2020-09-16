package conjugation

import data.Verb
import mode.Infinite

class InfiniteImpl(private var verb: Verb) : Infinite {

    override fun present(): String {
        return verb.infinitive
    }

    override fun perfect(): String {
        return "${verb.pastparticiple} ${verb.auxiliary}\n${verb.pastparticiple} zu ${verb.auxiliary}"
    }
}