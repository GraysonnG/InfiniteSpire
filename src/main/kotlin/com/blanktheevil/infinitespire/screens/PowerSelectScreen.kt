package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.vfx.DarkBgEffect
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.powers.AbstractPower

class PowerSelectScreen : Screen<PowerSelectScreen>() {
  companion object {
    private const val HB_SIZE = 128f
    private const val SPACING = 4f
    private const val HB_SIZE_SPACING = HB_SIZE + SPACING
    var powers: List<AbstractPower>? = null
    var hitboxes = List(8) { Hitbox(128f, 128f) }
  }

  var selectedPower: AbstractPower? = null
  private val darkBgEffect = DarkBgEffect(this)

  override fun updateScreen() {
    updateHiboxes()

    if (show) {
      if (this.selectedPower != null || powers.isNullOrEmpty()) {
        close()
      }
    }

    darkBgEffect.update()
  }

  private fun updateHiboxes() {
    if (!show) return

    fun updateHitbox(x: Int, y: Int, index: Int) {
      val hb = hitboxes[index]
      val xPos = Settings.WIDTH.div(2).minus(HB_SIZE_SPACING.times(2f).scale()).plus(HB_SIZE_SPACING.times(x).scale()).plus(SPACING.scale())
      val yPos = Settings.HEIGHT.div(2).minus(HB_SIZE_SPACING.times(y).scale())

      hb.move(Vector2(xPos, yPos))
      hb.update()
      if (hb.hovered && InputHelper.justClickedLeft) {
        selectedPower = powers!![index]
        callback.invoke(this)
      }
    }

    var index = 0
    for (y in 0 until 2) {
      for (x in 0 until 4) {
        updateHitbox(x, y, index)
        index++
      }
    }
  }

  override fun renderScreen(sb: SpriteBatch) {
    darkBgEffect.render(sb)

    if (!show) return

    FontHelper.renderFontCentered(
      sb,
      FontHelper.buttonLabelFont,
      "Select A Power",
      Settings.WIDTH.div(2f),
      Settings.HEIGHT.div(3f).times(2f),
      Color.WHITE.cpy()
    )

    if (powers != null) renderPowers(sb)
    hitboxes.forEach { it.render(sb) }
  }

  private fun renderPowers(sb: SpriteBatch) {
    fun renderPowerImg(sb: SpriteBatch, color: Color, powerImg: TextureAtlas.AtlasRegion, hb: Hitbox, additive: Boolean = false) {
      if (additive) sb.additiveMode()

      sb.color = color.cpy().also {
        if (additive) it.a = 0.5f
      }

      sb.draw(
        powerImg,
        hb.cX.minus(powerImg.regionWidth.div(2)),
        hb.cY.minus(powerImg.regionWidth.div(2)),
        powerImg.regionWidth.div(2f),
        powerImg.regionHeight.div(2f),
        powerImg.regionWidth.toFloat(),
        powerImg.regionHeight.toFloat(),
        scale,
        scale,
        0f
      )
      sb.normalMode()
    }

    fun renderHighlightBox(sb: SpriteBatch, hb: Hitbox) {
      sb.color = Color.WHITE.cpy().also {
        it.a = 0.3f
      }
      sb.draw(
        ImageMaster.WHITE_SQUARE_IMG,
        hb.x,
        hb.y,
        hb.width,
        hb.height
      )
    }

    var index = 0
    for (y in 0 until 2) {
      for (x in 0 until 4) {
        val power = powers!![index]
        val powerImg = power.region128
        val hb = hitboxes[index]
        if (hb.hovered) {
          renderHighlightBox(sb, hb)
        }
        renderPowerImg(sb, Color.WHITE, powerImg, hb)
        if (hb.hovered) {
          renderPowerImg(sb, Color.WHITE, powerImg, hb, true)
          TipHelper.renderGenericTip(hb.x.plus(hb.width), hb.y.plus(hb.height), power.name, power.description)
        }
        index++
      }
    }
  }

  override fun close() {
    show = false
    AbstractDungeon.isScreenUp = show
  }

  override fun open(callback: (screen: PowerSelectScreen) -> Unit) {
    open(emptyList(), callback)
  }

  fun open(powerList: List<AbstractPower>, callback: (powerSelectScreen: PowerSelectScreen) -> Unit = {}) {
    powers = powerList
    this.callback = callback
    selectedPower = null
    show = true
    AbstractDungeon.isScreenUp = show
  }
}