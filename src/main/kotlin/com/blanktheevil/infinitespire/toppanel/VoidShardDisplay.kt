package com.blanktheevil.infinitespire.toppanel

import basemod.TopPanelItem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.additiveMode
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.normalMode
import com.blanktheevil.infinitespire.extensions.scale
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.TipHelper

class VoidShardDisplay : TopPanelItem(IMG, ID) {
  companion object {
    private val IMG = Textures.ui.get("topPanel/avhari/voidShard.png")
    val ID = "VoidDisplay".makeID()
    private const val FLASH_ANIM_TIME = 2.0f
    private val TIP_Y_POS = Settings.HEIGHT.minus(120.0f.scale())
    private val STRINGS = CardCrawlGame.languagePack.getUIString("VoidShard")
  }

  var flashTimer = 0.0f

  override fun onClick() {
    CardCrawlGame.sound.play("RELIC_DROP_MAGICAL")
  }

  override fun update() {
    updateFlash()
    super.update()
  }

  override fun render(sb: SpriteBatch) {
    super.render(sb)
    renderFlash(sb)
    renderCount(sb)
  }

  fun flash() {
    this.flashTimer = FLASH_ANIM_TIME
  }

  private fun updateFlash() {
    if (flashTimer != 0.0f) {
      flashTimer -= Gdx.graphics.rawDeltaTime
    }
  }

  private fun renderCount(sb: SpriteBatch) {
    FontHelper.cardTitleFont.apply {
      data.setScale(1.0f)

      FontHelper.renderFontCentered(
        sb,
        this,
        InfiniteSpire.config.voidShards.toString(),
        x.plus(hb_w.div(2f)),
        y.plus(16f.scale()),
        Color.WHITE.cpy()
      )
    }

    if (this.getHitbox().hovered) {
      val xPos = this.x.plus(this.hb_w.div(2))
      TipHelper.renderGenericTip(
        xPos, TIP_Y_POS, STRINGS.TEXT[1], STRINGS.TEXT[5]
      )
    }
  }

  private fun renderFlash(sb: SpriteBatch) {
    val tmp = Interpolation.exp10In.apply(0f, 4f, flashTimer / FLASH_ANIM_TIME)

    sb.additiveMode()
    sb.color = Color.WHITE.cpy().also { it.a = flashTimer * FLASH_ANIM_TIME }

    renderImageWithScale(sb, Settings.scale.plus(tmp))
    renderImageWithScale(sb, Settings.scale.plus(tmp.times(0.66f)))
    renderImageWithScale(sb, Settings.scale.plus(tmp.div(3f)))

    sb.normalMode()
  }

  private fun renderImageWithScale(sb: SpriteBatch, scale: Float) {
    val halfWidth = this.image.width.div(2f)
    val halfHeight = this.image.height.div(2f)

    sb.draw(
      image,
      this.x.minus(halfWidth).plus(halfHeight).scale(),
      this.y.minus(halfHeight).plus(halfWidth).scale(),
      halfWidth,
      halfHeight,
      this.image.width.toFloat(),
      this.image.height.toFloat(),
      scale,
      scale,
      this.angle,
      0,
      0,
      this.image.width,
      this.image.height,
      false,
      false
    )
  }
}