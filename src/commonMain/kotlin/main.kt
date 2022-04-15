import com.soywiz.klock.TimeSpan
import com.soywiz.korge.*
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.baseview.BaseView
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launch
import com.soywiz.korma.random.get
import ui_components.UIWordLine
import kotlin.random.Random

data class WordData(
    val englishForm: String,
    val data: String
)

inline fun <reified T : Any> BaseView.getProp(key: String): T {
    return props[key]!! as T
}

val HIRAGANA_WORDS = mutableListOf(
    WordData("a", "あ"),
    WordData("i", "い"),
    WordData("u", "う"),
    WordData("e", "え"),
    WordData("o", "お"),
    WordData("ka", "か"),
    WordData("ki", "き"),
    WordData("ku", "く"),
    WordData("ke", "け"),
    WordData("ko", "こ"),
    WordData("sa", "さ"),
    WordData("shi", "し"),
    WordData("su", "す"),
    WordData("se", "せ"),
    WordData("so", "そ"),
    WordData("ta", "た"),
    WordData("chi", "ち"),
    WordData("tsu", "つ"),
    WordData("te", "て"),
    WordData("to", "と"),
    WordData("na", "な"),
    WordData("ni", "に"),
    WordData("nu", "ぬ"),
    WordData("ne", "ね"),
    WordData("no", "の"),
    WordData("ha", "は"),
    WordData("hi", "ひ"),
    WordData("fu", "ふ"),
    WordData("he", "へ"),
    WordData("ho", "ほ"),
    WordData("ma", "ま"),
    WordData("mi", "み"),
    WordData("mu", "む"),
    WordData("me", "め"),
    WordData("mo", "も"),
    WordData("ya", "や"),
    WordData("yu", "ゆ"),
    WordData("yo", "よ"),
    WordData("ra", "ら"),
    WordData("ri", "り"),
    WordData("ru", "る"),
    WordData("re", "れ"),
    WordData("ro", "ろ"),
    WordData("wa", "わ"),
    WordData("wo", "を"),
    WordData("n", "ん"),
)

@OptIn(KorgeExperimental::class)
suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
    RESOURCES.initialize()

    val WORDS_PER_LINE = 10

    val views = injector.get<Views>()

    val lastWords = mutableSetOf<WordData>()

    fun getNextWords(): List<WordData> {
        return mutableSetOf<WordData>().apply {
            while (size < WORDS_PER_LINE) {
                val word = HIRAGANA_WORDS.getRandom()
                add(word)
                lastWords.add(word)
            }
        }.toList()
    }

    var words = getNextWords()
    var words2 = getNextWords()

    lateinit var currentWordLine: UIWordLine
    lateinit var nextWordLine: UIWordLine
    val wordLinesContainer = container {
        currentWordLine = UIWordLine(words).addTo(this)
        nextWordLine = UIWordLine(words2).addTo(this)
        nextWordLine.alignTopToBottomOf(currentWordLine)
    }

    currentWordLine.updateWordSelectedRect(0)

    wordLinesContainer.centerOnStage()

    val textInput = uiTextInput("") {
        alignTopToBottomOf(wordLinesContainer)
        centerXOn(wordLinesContainer)

        onTextUpdated {
            val text = it.text
            if (text.isEmpty()) return@onTextUpdated
            if (text.last() == ' ') {
                currentWordLine.inputWord(text.substring(0, text.length - 1))
                it.text = ""
            }

            if (currentWordLine.isFinished) {
                println("Word line is exhausted!")
                val prevLine = currentWordLine
                prevLine.visible = false
                currentWordLine = nextWordLine
                currentWordLine.updateWordSelectedRect(0)
                lastWords.removeAll(words.toSet())
                words = words2
                words2 = getNextWords()
                launch {
                    currentWordLine.moveTo(prevLine.x, prevLine.y, time = TimeSpan(100.0))
                    nextWordLine = UIWordLine(words2).addTo(wordLinesContainer) {
                        alignTopToBottomOf(currentWordLine)
                    }
                }
            }
        }
    }

}

private fun <T : Any> List<T>.getRandom(): T {
    return get(Random[0, size])
}
