package com.blanktheevil.infinitespire.ui.buttons

import basemod.IUIElement
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox

class ColoredButton(
  val text: String = "",
  private val xPos: Float = Settings.WIDTH.div(2f),
  private val yPos: Float = Settings.HEIGHT.div(2f),
  val color: Color = Color(1f, 1f, 1f, 1f),
  val hb: Hitbox = Hitbox(xPos, yPos, 0f, 0f),
  val scale: Float = 1f,
  private val onClick: ColoredButton.() -> Unit = {}
) : SpireElement, SpireClickable, IUIElement {
  companion object {
    private val font = FontHelper.buttonLabelFont
  }

  private var textWidth = 0f
  private var buttonLeft = Textures.ui.get("button/ButtonLeft.png").asAtlasRegion()
  private var buttonMiddle = Textures.ui.get("button/ButtonMiddle.png").asAtlasRegion()
  private var buttonRight = Textures.ui.get("button/ButtonRight.png").asAtlasRegion()
  private var leftOffset = buttonLeft.packedWidth.scale()
  private var totalOffset =
    buttonLeft.packedWidth.scale().div(2)
      .plus(textWidth.div(2)).plus(buttonRight.packedWidth.scale().div(2))
  private var h = buttonLeft.packedHeight.scale()
  private var w = 0f

  init {
    hb.height = h
    hb.width = w
  }

  override fun update() {
    textWidth = FontHelper.getSmartWidth(font, text, 9999f, 0.0f)
    textWidth = 0f.coerceAtLeast(textWidth)
    totalOffset = buttonLeft.packedWidth.scale().div(2)
      .plus(textWidth.div(2)).plus(buttonRight.packedWidth.scale().div(2)).times(scale)
    hb.x = xPos.minus(totalOffset)
    hb.width = w.plus(buttonLeft.packedWidth).plus(buttonRight.packedWidth).plus(textWidth).times(scale)
    hb.height = h.times(scale)
    hb.update()
    if (hb.justHovered) {
      CardCrawlGame.sound.play("UI_HOVER")
    }
    if (leftClicked()) {
      CardCrawlGame.sound.play("UI_CLICK_1")
      onClick.invoke(this)
    }
  }

  override fun render(sb: SpriteBatch) {
    sb.color = color
    sb.draw(
      buttonLeft,
      xPos.minus(totalOffset),
      yPos,
      buttonLeft.packedWidth.scale().times(scale),
      h.times(scale)
    )
    sb.draw(
      buttonMiddle,
      xPos.plus(leftOffset).minus(totalOffset),
      yPos,
      textWidth.times(scale),
      h.times(scale)
    )
    sb.draw(
      buttonRight,
      xPos.plus(leftOffset).plus(textWidth).minus(totalOffset),
      yPos,
      buttonRight.packedWidth.scale().times(scale),
      h.times(scale)
    )

    hb.render(sb)

    sb.color = Color.WHITE

    FontHelper.renderFontCentered(
      sb, font, text, xPos, yPos.plus(h.times(scale).div(2)), sb.color
    )
  }

  override fun getHitbox(): Hitbox = hb
  override fun updateOrder(): Int = 1
  override fun renderLayer(): Int = 1
}