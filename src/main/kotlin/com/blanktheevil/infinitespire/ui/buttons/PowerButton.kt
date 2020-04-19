package com.blanktheevil.infinitespire.ui.buttons

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.additiveMode
import com.blanktheevil.infinitespire.extensions.move
import com.blanktheevil.infinitespire.extensions.normalMode
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.powers.AbstractPower

class PowerButton(
  var x: Float,
  var y: Float,
  var w: Float = HB_SIZE,
  var h: Float = HB_SIZE,
  val hb: Hitbox = Hitbox(x, y, w, h),
  val power: AbstractPower? = null,
  private val onClick: (power: AbstractPower?) -> Unit = {}
) : SpireElement, SpireClickable {
  companion object {
    private const val HB_SIZE = 128f
    private const val SPACING = 4f
    private const val HB_SIZE_SPACING = HB_SIZE + SPACING
  }

  override fun update() {
    hb.update()
  }

  fun updateButton(x: Int, y: Int) {
    val xPos = Settings.WIDTH.div(2).minus(HB_SIZE_SPACING.times(2f).scale()).plus(HB_SIZE_SPACING.times(x).scale()).plus(SPACING.scale())
    val yPos = Settings.HEIGHT.div(2).minus(HB_SIZE_SPACING.times(y).scale())

    this.hb.move(Vector2(xPos, yPos))
    update()
    if (leftClicked()) {
      onClick.invoke(power)
    }
  }

  override fun render(sb: SpriteBatch) {
    if (hb.hovered) renderHighlightBox(sb)
    if (power == null) return
    renderPowerImage(sb, Color.WHITE)
    if (hb.hovered) {
      renderPowerImage(sb, Color.WHITE, true)
      TipHelper.renderGenericTip(
        hb.x.plus(hb.width),
        hb.y.plus(hb.height),
        power.name,
        power.description
      )
    }
    hb.render(sb)
  }

  private fun renderPowerImage(sb: SpriteBatch, color: Color, additive: Boolean = false) {
    if (additive) sb.additiveMode()

    sb.color = color.cpy().also {
      if (additive) it.a = 0.5f
    }

    val powerImg = power!!.region128

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

  private fun renderHighlightBox(sb: SpriteBatch) {
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

  override fun getHitbox(): Hitbox = hb
}