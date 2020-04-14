package com.blanktheevil.infinitespire.textures

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.InfiniteSpire

class TextureGetter(private val folder: String) {
  fun get(texture: String): Texture = TextureLoaderKt.getTexture(getString(texture))
  fun getString(texture: String, ignoreValidation: Boolean = false): String {
    return when {
      ignoreValidation -> getString(folder, texture)
      TextureLoaderKt.exists(getString(folder, texture)) -> getString(folder, texture)
      else -> Textures.missingTexturePath
    }
  }

  companion object {
    private fun getString(folder: String, texture: String) = InfiniteSpire.createPath("$folder/") + texture
  }
}