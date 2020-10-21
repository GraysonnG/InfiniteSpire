package com.blanktheevil.infinitespire.patches.gdx

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics
import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch

@SpirePatch(clz = LwjglGraphics::class, method = "getRawDeltaTime")
object RawDeltaTimePausePatch {
  @JvmStatic
  @SpirePostfixPatch
  fun pauseOverride(result: Float, instance: Graphics): Float = if (InfiniteSpire.pauseGame) 0f else result
}