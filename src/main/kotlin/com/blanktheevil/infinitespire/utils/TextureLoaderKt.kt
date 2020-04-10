package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.Textures
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import org.apache.logging.log4j.LogManager

class TextureLoaderKt {
  companion object {
    private val textures = mutableMapOf<String, Texture>()
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
        loadTexture(texturePath)
        true
      } catch (e: Exception) {
        logger.error("Could not find texture: $texturePath")
        e.printStackTrace()
        false
      }
    }

    private fun loadTexture(texturePath: String): Texture {
      return Texture(texturePath).also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        textures[texturePath] = it
      }
    }
  }

  @Suppress("unused")
  @SpirePatch(clz = Texture::class, method = "dispose")
  class DisposeListener {
    companion object {
      @JvmStatic
      @SpirePrefixPatch
      fun listen(__instance: Texture) {
        textures.entries.removeIf {
          it.value == __instance
        }
      }
    }
  }
}