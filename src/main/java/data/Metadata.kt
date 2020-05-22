package data

data class Metadata(private var verb: Verb) {

    val verbclass = verb.verbclass
    val reflexity = verb.reflexivity
    val complexity = verb.complexity
    val separability = verb.separability
    val pastform = verb.past
    val pastparticiple = verb.pastparticiple
}
