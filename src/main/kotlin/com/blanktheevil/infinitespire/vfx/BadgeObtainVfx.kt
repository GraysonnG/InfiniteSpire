package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class BadgeObtainVfx(private val badge: Badge) : AbstractGameEffect() {
  companion object {
    private val BG_TEXTURE = Textures.vfx.get("form-bg.png").asAtlasRegion()
  }

  private val startingHeight = (-200f).scale()
  private val endingHeight = Settings.HEIGHT.div(2f)
  private var height = startingHeight

  private var bgRotation = 0f
  private var bgScale = 0.5f
  private var bgOpacity = 0f

  init {
    this.startingDuration = Settings.ACTION_DUR_XLONG
    this.duration = 0f
  }

  override fun dispose() {
  }

  override fun update() {
    val half = duration.times(2f).div(startingDuration).clamp(0f, 1f)

    val progress = duration / startingDuration

    height = Interpolation.pow5Out.apply(startingHeight, endingHeight, half)
    bgRotation = Interpolation.pow5Out.apply(0f, 90f, progress)
    bgScale = Interpolation.pow5Out.apply(0.5f, 1f, progress)
    bgOpacity = Interpolation.pow5Out.apply(0f, 1f, progress)

    this.duration += deltaTime
    if (duration > startingDuration) {
      this.isDone = true
    }
  }

  override fun render(sb: SpriteBatch) {
    sb.color = InfiniteSpire.PURPLE.cpy().also { it.a = bgOpacity }
    sb.additiveMode()
    sb.draw(
      BG_TEXTURE,
      Settings.WIDTH.div(2f).minus(BG_TEXTURE.packedWidth.div(2f)),
      Settings.HEIGHT.div(2f).minus(BG_TEXTURE.packedHeight.div(2f)),
      BG_TEXTURE.packedWidth.div(2f),
      BG_TEXTURE.packedHeight.div(2f),
      BG_TEXTURE.packedWidth.toFloat(),
      BG_TEXTURE.packedHeight.toFloat(),
      bgScale,
      bgScale,
      bgRotation
    )
    sb.normalMode()

    // TODO redo this
    FontHelper.renderFontCentered(
      sb,
      FontHelper.bannerNameFont,
      badge.name,
      Settings.WIDTH.div(2f),
      height.plus(35f.scale()),
      Color.WHITE.cpy()
    )

    FontHelper.renderFontCentered(
      sb,
      FontHelper.menuBannerFont,
      badge.description,
      Settings.WIDTH.div(2f),
      height.minus(35f.scale()),
      Color.WHITE.cpy()
    )
  }
}