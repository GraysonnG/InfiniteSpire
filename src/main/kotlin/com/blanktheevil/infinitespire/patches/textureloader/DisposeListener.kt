package com.blanktheevil.infinitespire.patches.textureloader

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.utils.TextureLoaderKt
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch

@Suppress("unused")
@SpirePatch(clz = Texture::class, method = "dispose")
object DisposeListener {
  @JvmStatic
  @SpirePrefixPatch
  fun listen(__instance: Texture) {
    TextureLoaderKt.textures.entries.removeIf {
      it.value == __instance
    }
  }
}