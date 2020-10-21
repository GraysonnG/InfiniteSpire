package com.blanktheevil.infinitespire.textures

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.InfiniteSpire

class TextureGetter(private val folder: String) {
  fun get(texture: String): Texture = TextureLoaderKt.getTexture(getString(texture, true))
  fun getString(texture: String, ignoreValidation: Boolean = false): String {
    val finalString = getString(folder, texture)
    val validated = if (!ignoreValidation) TextureLoaderKt.exists(finalString) else true

    return when {
      validated -> finalString
      else -> Textures.missingTexturePath
    }
  }

  companion object {
    private fun getString(folder: String, texture: String) = InfiniteSpire.createPath("$folder/") + texture
  }
}