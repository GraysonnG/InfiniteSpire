package com.blanktheevil.infinitespire.textures

import com.badlogic.gdx.graphics.Texture
import org.apache.logging.log4j.LogManager

object TextureLoaderKt {
  val textures = mutableMapOf<String, Texture>()
  private val logger = LogManager.getLogger(TextureLoaderKt::class.java.name)

  fun getTexture(texturePath: String): Texture {
    return if (textures[texturePath] != null) {
      textures[texturePath]!!
    } else {
      try {
        loadTexture(texturePath)
      } catch (e: Exception) {
        logger.error("Could not find texture: $texturePath")
        Textures.missingTexture
      }
    }
  }

  fun exists(texturePath: String): Boolean {
    return try {
      val dispose: Texture = loadTexture(texturePath)
      true
    } catch (e: Exception) {
      logger.error("Could not find texture: $texturePath")
      e.printStackTrace()
      false
    }
  }

  private fun loadTexture(texturePath: String): Texture {
    return Texture(texturePath).also {
      it.setFilter(
        Texture.TextureFilter.Linear,
        Texture.TextureFilter.Linear
      )
      textures[texturePath] = it
    }
  }
}
