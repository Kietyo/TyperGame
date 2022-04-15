import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.font.readTtfFont
import com.soywiz.korio.file.std.resourcesVfs

val RESOURCES = Resources()

class Resources {
    lateinit var JAPANESE_FONT: TtfFont

    suspend fun initialize() {
        JAPANESE_FONT = resourcesVfs["LogoTypeGothicCondense/font.ttf"].readTtfFont()
    }
}