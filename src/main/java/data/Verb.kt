package data

import java.lang.StringBuilder

data class Verb(
    var infinitive: String,
    var past: String,
    var pastparticiple: String,
    var ending: String,
    var stemalternation: String?,
    var prefix: String?,
    var auxiliary: String,
    var reflexivity: String,
    var verbclass: String,
    var complexity: String,
    var separability: String
) {

    fun reduceStem(): String {
        return StringBuilder(infinitive)
            .apply {
                reverse()
                replace(0, ending.length, "")
                reverse()
                if (separability == "separable")
                    prefix?.length?.let { delete(0, it) }
            }.toString()
    }

    fun reduceStemTest(): MutableList<String> {
        return mutableListOf<String>()
            .apply {
                val stem = StringBuilder(infinitive).apply {
                    reverse()
                    replace(0, ending.length, "")
                    reverse()
                }
                if (separability == "separable")
                    prefix?.length?.let { stem.delete(0, it) }


                repeat((0..5).count()) {
                    var alterStem = stem
                    when {
                        stemalternation != null && (it == 1 || it == 2) -> {
                            alterStem = StringBuilder(checkStemAlternation(alterStem.toString()))
                            this.add(alterStem.toString())
                        }
                        else -> {
                            this.add(stem.toString())
                        }
                    }
                }

            }.toMutableList()
    }

    private fun checkStemAlternation(reducedStem: String): String {
        when (stemalternation) {
            "a=ä", "e=i", "o=i", "o=ö", "ö=a", "ü=u", "ü=a" -> return stemalternation!![0].let {
                reducedStem.replace(
                    it,
                    stemalternation!![2]
                )
            }
            "e=ie" -> return reducedStem.replace("e", "ie")
        }
        return reducedStem
    }

    fun getStemEnding(): String {
        when (ending) {
            "en" -> when {
                reduceStem().endsWith("chs") -> return reduceStem().substring(reduceStem().length - "chs".length)
                reduceStem().endsWith("d") -> return reduceStem().substring(reduceStem().length - "d".length)
                reduceStem().endsWith("s") -> return reduceStem().substring(reduceStem().length - "s".length)
                reduceStem().endsWith("ss") -> return reduceStem().substring(reduceStem().length - "ss".length)
                reduceStem().endsWith("ß") -> return reduceStem().substring(reduceStem().length - "ß".length)
                reduceStem().endsWith("t") -> return reduceStem().substring(reduceStem().length - "t".length)
                reduceStem().endsWith("tz") -> return reduceStem().substring(reduceStem().length - "tz".length)
                reduceStem().endsWith("tm") -> return reduceStem().substring(reduceStem().length - "tm".length)
                reduceStem().endsWith("x") -> return reduceStem().substring(reduceStem().length - "x".length)
                reduceStem().endsWith("z") -> return reduceStem().substring(reduceStem().length - "z".length)
            }
        }
        return reduceStem()
    }
}