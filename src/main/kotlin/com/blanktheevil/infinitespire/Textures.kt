package com.blanktheevil.infinitespire

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.utils.TextureGetter

@Suppress("unused")
abstract class Textures {
  companion object {
    private fun getString(folder: String, texture: String) = InfiniteSpire.createPath("$folder/") + texture
    val cards = TextureGetter("cards")
    val events = TextureGetter("events")
    val monsters = TextureGetter("monsters")
    val powers = TextureGetter("powers")
    val relics = TextureGetter("relics")
    val screen = TextureGetter("screen")
    val ui = TextureGetter("ui")
    val vfx = TextureGetter("vfx")
    val missingTexture by lazy { Texture(InfiniteSpire.createPath(missingTexturePath)) }
    val missingTexturePath = ui.getString("missingtexture.png", true)
  }
}