package data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import conjugation.*
import java.io.*
import java.util.*
import java.util.zip.GZIPOutputStream

class DataLoader {

    companion object {
        //val fc = JFileChooser("D:")
        private fun jsonParser(): Queue<Verb> {
            val gson = Gson()
            val verbs: Queue<Verb>
            val reader = FileReader("D:\\Verbenkonjugator\\test\\kompletteverben.json")
            //fc.showOpenDialog(null)
            //val reader = FileReader(fc.selectedFile.toString())
            verbs = gson.fromJson(reader, object : TypeToken<LinkedList<Verb>>() {}.type)
            return verbs
        }

        fun jsonWriter(conjugation: Map<String, List<Map<String, String>>>) {
            val writer = FileWriter("D:\\Verbenkonjugator\\test\\verbenliste.json")
            val gson = GsonBuilder().setPrettyPrinting().create()
            gson.toJson(conjugation, writer)
            writer.close()
        }

        fun compressData() {
            val output = "D:\\Verbenkonjugator\\test\\kompletteverben.bin"
            val input = "D:\\Verbenkonjugator\\test\\verbenliste.json"
            val buffer = ByteArray(1024)

            val gzipOutputStream = GZIPOutputStream(FileOutputStream(output))
            val fileInputStream = FileInputStream(input)

            var length: Int

            while (fileInputStream.read(buffer).also { length = it } > 0) {
                gzipOutputStream.write(buffer, 0, length)
            }
            gzipOutputStream.finish()
            gzipOutputStream.close()

        }

        fun complete(): MutableMap<String, MutableList<MutableMap<String, String>>> {
            val data = jsonParser()
            var tenseMap: MutableMap<String, String> = linkedMapOf()
            val tenseList: MutableList<MutableMap<String, String>> = arrayListOf()
            val modesMap: MutableMap<String, MutableList<MutableMap<String, String>>> = linkedMapOf()

            data.map {
                it.apply {

                    val indicative = IndicativeImpl(it)
                    val subjunctive = SubjunctiveImpl(it)
                    val imperative = ImperativeImpl(it)
                    val infinite = InfiniteImpl(it)
                    val participle = ParticipleImpl(it)
                    val metadata = Metadata(it)

                    tenseMap["indicativePresent"] = indicative.present()
                    tenseMap["indicativePast"] = indicative.past()
                    tenseMap["indicativePerfect"] = indicative.perfect()
                    tenseMap["indicativePluPerfect"] = indicative.pluperfect()
                    tenseMap["indicativeFutureOne"] = indicative.futureOne()
                    tenseMap["indicativeFutureTwo"] = indicative.futureTwo()
                    tenseMap["subjunctiveOne"] = subjunctive.one()
                    tenseMap["subjunctiveTwo"] = subjunctive.two()
                    tenseMap["subjunctivePerfect"] = subjunctive.perfect()
                    tenseMap["subjunctivePluPerfect"] = subjunctive.pluperfect()
                    tenseMap["subjunctiveFutureOne"] = subjunctive.futureOne()
                    tenseMap["subjunctiveFutureTwo"] = subjunctive.futureTwo()
                    tenseMap["imperative"] = imperative.imp()
                    tenseMap["infinitivePresent"] = infinite.present()
                    tenseMap["infinitivePerfect"] = infinite.perfect()
                    tenseMap["participleOne"] = participle.one()
                    tenseMap["participleTwo"] = participle.two()
                    tenseMap["verbclass"] = metadata.verbclass
                    tenseMap["reflexivity"] = metadata.reflexity
                    tenseMap["complexity"] = metadata.complexity
                    tenseMap["separability"] = metadata.separability
                    tenseMap["pastform"] = metadata.pastform
                    tenseMap["pastparticiple"] = metadata.pastparticiple
                    tenseList.add(tenseMap)
                    modesMap["verben"] = tenseList
                    tenseMap = linkedMapOf()
                }
            }
            return modesMap
        }
    }
}