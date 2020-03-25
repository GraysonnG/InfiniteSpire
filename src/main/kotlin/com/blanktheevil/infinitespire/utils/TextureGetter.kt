package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.InfiniteSpire

class TextureGetter(private val folder: String) {
  fun get(texture: String): Texture = TextureLoaderKt.getTexture(getString(texture))
  fun getString(texture: String): String = getString(folder, texture)

  companion object {
    private fun getString(folder: String, texture: String) = InfiniteSpire.createPath("$folder/") + texture
  }
}