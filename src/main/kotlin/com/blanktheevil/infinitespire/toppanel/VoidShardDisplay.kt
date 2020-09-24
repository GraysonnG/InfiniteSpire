package com.blanktheevil.infinitespire.toppanel

import basemod.TopPanelItem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.addVoidShard
import com.blanktheevil.infinitespire.utils.subVoidShard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.TipHelper

class VoidShardDisplay : TopPanelItem(IMG, ID), SpireClickable {
  companion object {
    private val IMG = Textures.ui.get("topPanel/avhari/voidShard.png")
    private val REGION = IMG.asAtlasRegion()
    val ID = "VoidDisplay".makeID()
    private const val FLASH_ANIM_TIME = 2.0f
    private val TIP_Y_POS = Settings.HEIGHT.minus(120.0f.scale())
    private val STRINGS = languagePack.getUIString("VoidShard")
  }

  var flashTimer = 0.0f

  override fun isClickable(): Boolean = false

  override fun onClick() {
    if (Settings.isDebug) addVoidShard(1)
    CardCrawlGame.sound.play("RELIC_DROP_MAGICAL")
  }

  override fun update() {
    updateFlash()
    super.update()

    with(getHitbox()) {
      cX = x.plus(width.div(2f))
      cY = y.plus(height.div(2f))
    }

    if (Settings.isDebug && rightClicked()) {
      subVoidShard(1)
    }
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
    if (flashTimer >= 0.0f) {
      flashTimer -= Gdx.graphics.rawDeltaTime
    }
    flashTimer = flashTimer.clamp(0f, FLASH_ANIM_TIME)
  }

  private fun renderCount(sb: SpriteBatch) {
    FontHelper.cardTitleFont.apply {
      data.setScale(1.0f)

      FontHelper.renderFontCentered(
        sb,
        this,
        InfiniteSpire.saveData.voidShards.count.toString(),
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
    val tmp = Interpolation.exp10In.apply(1f, 4f, flashTimer / FLASH_ANIM_TIME)

    sb.additiveMode()
    sb.color = Color.WHITE.cpy().also { it.a = flashTimer / FLASH_ANIM_TIME }

    renderImageWithScale(sb, scale.plus(tmp))
    renderImageWithScale(sb, scale.plus(tmp.times(0.66f)))
    renderImageWithScale(sb, scale.plus(tmp.div(3f)))

    sb.normalMode()
  }

  private fun renderImageWithScale(sb: SpriteBatch, imgScale: Float) {
    with(REGION) {
      sb.draw(
        this,
        getHitbox().cX.minus(halfWidth),
        getHitbox().cY.minus(halfHeight),
        halfWidth,
        halfHeight,
        width,
        height,
        imgScale,
        imgScale,
        angle
      )
    }
  }
}