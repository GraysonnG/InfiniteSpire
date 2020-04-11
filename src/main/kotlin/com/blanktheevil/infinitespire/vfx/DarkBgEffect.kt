package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.extensions.clamp
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.screens.Screen
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster

class DarkBgEffect<T>(val screen: Screen<T>) : SpireElement {
  companion object {
    const val SCREEN_BG_OPEN_TARGET = 0.65f
  }

  private val bgColor = Color.BLACK.cpy().also { it.a = 0f }
  private var bgColorInterpProgress = 0f

  override fun update() {
    bgColorInterpProgress += if (screen.show) Gdx.graphics.rawDeltaTime.times(3) else -Gdx.graphics.rawDeltaTime.times(3)
    bgColorInterpProgress = bgColorInterpProgress.clamp(0f, 1f)

    bgColor.a = Interpolation.fade.apply(0f, SCREEN_BG_OPEN_TARGET, bgColorInterpProgress)
  }

  override fun render(sb: SpriteBatch) {
    with(sb) {
      color = this@DarkBgEffect.bgColor
      draw(
        ImageMaster.WHITE_SQUARE_IMG,
        0f,
        0f,
        Settings.WIDTH.toFloat(),
        Settings.HEIGHT.toFloat()
      )
    }
  }
}