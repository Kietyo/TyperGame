package ui_components

import RESOURCES
import WordData
import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.readTtfFont
import com.soywiz.korio.async.runBlockingNoSuspensions
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle


class UIWord(
    val wordData: WordData
) : Container() {
    val WORD_WIDTH = 20.0
    val correctText = text(wordData.englishForm, font = RESOURCES.JAPANESE_FONT, textSize = 20.0) {
        visible = false
    }
    val text = text(wordData.data, font = RESOURCES.JAPANESE_FONT, textSize = 30.0) {
    }
    init {
        text.alignTopToBottomOf(correctText)
        text.centerXOn(correctText)
    }

    fun displayCorrectText() {
        correctText.visible = true
    }

    fun setTextColor(c: RGBA) {
        text.color = c
    }
}