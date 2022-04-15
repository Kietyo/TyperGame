package ui_components

import WordData
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors

class UIWordLine(val words: List<WordData>): Container() {
    val SELECTION_RECT_LEFT_RIGHT_PADDING = 10.0

    var currentWordIndex: Int? = null
    var isFinished = false

    val wordSelectedRect = solidRect(100, 100, color = Colors.WHITE.withAd(0.5)) {
        visible = false
    }
    val wordContainer = container wordContainer@{
        repeat(10) {
            val randomWord = words[it]
            UIWord(randomWord).addTo(this@wordContainer)
        }

        forEachChildWithIndex { index, child ->
            if (index == 0) return@forEachChildWithIndex
            val prev = this[index - 1]
            child.alignLeftToRightOf(prev, padding = 20.0)
        }
    }

    fun updateWordSelectedRect(idx: Int?) {
        currentWordIndex = idx
        if (idx == null) {
            wordSelectedRect.visible = false
            return
        }
        if (idx >= words.size) {
            currentWordIndex = null
            wordSelectedRect.visible = false
            isFinished = true
            return
        }
        wordSelectedRect.visible = true
        val uiWord = wordContainer[idx] as UIWord
        with(wordSelectedRect) {
            scaledWidth = uiWord.text.scaledWidth + SELECTION_RECT_LEFT_RIGHT_PADDING
            scaledHeight = uiWord.text.scaledHeight
            centerOn(uiWord.text)
        }
    }

    fun inputWord(s: String) {
        if (currentWordIndex == null) {
            return
        }

        val uiWord = wordContainer[currentWordIndex!!] as UIWord
        if (s.lowercase() == uiWord.wordData.englishForm) {
            uiWord.setTextColor(Colors.GREEN)
        } else {
            uiWord.setTextColor(Colors.RED)
            uiWord.displayCorrectText()
        }
        currentWordIndex = currentWordIndex!! + 1
        updateWordSelectedRect(currentWordIndex)
    }
}