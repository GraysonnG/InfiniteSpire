package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ScreenShake
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.atan2

class BlackStrikeLargeVfx(val hb: Hitbox) : AbstractGameEffect()  {
  companion object {
    private val IMG: TextureAtlas.AtlasRegion by lazy {
      Textures.vfx.get("strike/fist-l.png").asAtlasRegion()
    }
    private const val DURATION = 1f
  }

  private val xEnd = hb.cX.minus(IMG.packedWidth.div(2f))
  private val yEnd = hb.cY.minus(IMG.packedHeight.div(2f))
  private val xStart = 0f
  private val yStart = Settings.HEIGHT.toFloat()
  private val scaleStart = Settings.scale.div(2f)
  private val scaleEnd = Settings.scale
  private var angle = 0f
  private var hasSoundPlayed = false
  private var hasScreenShook = false
  private var x = xStart
  private var y = yStart
  private val color2 = InfiniteSpire.PURPLE.cpy()
  private val color3 = InfiniteSpire.RED.cpy()

  init {
    this.duration = DURATION
    this.startingDuration = DURATION
    this.color = Color.BLACK.cpy().also { it.a = 0f }
    this.scale = scaleStart
  }

  override fun update() {
    this.duration -= deltaTime

    val oneThird = this.startingDuration / 3f
    val twoThirds = oneThird * 2f

    when {
      duration <= twoThirds && duration > oneThird -> {
        if (!hasScreenShook) {
          CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.LONG, true)
          hasScreenShook = true
        }
        updateData()
      }
      duration <= oneThird -> {
        if (!hasSoundPlayed) {
          updateData()
          CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.LONG, true)
          CardCrawlGame.sound.playA("ATTACK_IRON_2", -0.5f)
          this.hasSoundPlayed = true
        }
        this.scale = Settings.scale.times(MathUtils.random(0.9f, 1f))
      }
    }

    if (duration <= 0f) {
      this.isDone = true
    }
  }

  private fun updateData() {
    val oneThird = this.startingDuration / 3f
    val percentAlong = 1 - ((this.duration - oneThird) / oneThird).clamp(0f, 1f)
    val colorAlpha = Interpolation.circleIn.apply(0f, 1f, percentAlong)
    color.a = colorAlpha
    color2.a = colorAlpha
    color3.a = colorAlpha
    x = Interpolation.circleIn.apply(xStart, xEnd.minus(IMG.packedHeight.div(4f)), percentAlong)
    y = Interpolation.circleOut.apply(yStart, yEnd, percentAlong)
    scale = Interpolation.circleIn.apply(scaleStart, scaleEnd, percentAlong)
    val deltaX = xEnd - x
    val deltaY = yEnd - y
    angle = Math.toDegrees(atan2(deltaY, deltaX).toDouble()).toFloat()
    color.a = color.a.clamp(0f, 1f)
  }

  override fun render(sb: SpriteBatch) {
    drawImageWithColor(sb, color3, 1.2f)
    drawImageWithColor(sb, color2, 1.1f)
    drawImageWithColor(sb, color)
  }

  private fun drawImageWithColor(sb: SpriteBatch, color: Color, scaleOffset: Float = 1f) {
    sb.color = color
    sb.draw(
      IMG,
      x,
      y,
      IMG.packedWidth.div(2f),
      IMG.packedHeight.div(2f),
      IMG.packedWidth.toFloat(),
      IMG.packedHeight.toFloat(),
      scale.times(scaleOffset),
      scale.times(scaleOffset),
      angle.minus(90f)
    )
  }

  override fun dispose() = doNothing()
}