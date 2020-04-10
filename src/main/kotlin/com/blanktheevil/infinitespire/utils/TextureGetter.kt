package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.InfiniteSpire

class TextureGetter(private val folder: String) {
  fun get(texture: String): Texture = TextureLoaderKt.getTexture(getString(texture))
  fun getString(texture: String, ignoreValidation: Boolean = false): String {
    return when {
      ignoreValidation -> getString(folder, texture)
      TextureLoaderKt.exists(getString(folder, texture)) -> getString(folder, texture)
      else -> "img/infinitespire/ui/missingtexture.png"
    }
  }

  companion object {
    private fun getString(folder: String, texture: String) = InfiniteSpire.createPath("$folder/") + texture
  }
}