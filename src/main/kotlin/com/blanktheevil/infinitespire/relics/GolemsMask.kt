package com.blanktheevil.infinitespire.relics

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.scale
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.relics.AbstractRelic
import kotlin.math.ceil

class GolemsMask : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Golems Mask".makeID()
    private const val IMG = "golemsstart"
    private val TIER = RelicTier.RARE
    private val SOUND = LandingSound.FLAT
    private var offsetX: Float? = null
  }

  init {
    counter = -1
  }

  private fun display(): String = "+${counter.times(10)}%"

  override fun atDamageModify(damage: Float, c: AbstractCard): Float =
    damage.plus(ceil(damage.times(counter.toFloat().times(0.10f))))

  override fun atTurnStart() {
    this.flash()
    counter++
  }

  override fun atBattleStart() {
    counter = -1
  }

  override fun onVictory() {
    counter = -1
  }

  override fun renderCounter(sb: SpriteBatch, inTopPanel: Boolean) {
    if (offsetX == null) {
      offsetX = ReflectionHacks.getPrivateStatic(AbstractRelic::class.java, "offsetX") as Float
    }

    if (counter > -1) {
      val x = if (inTopPanel) {
        (offsetX ?: 0f) + currentX - 15.scale()
      } else {
        currentX + 10.scale()
      }
      val y = currentY - 13.scale()
      val c = if (counter > 0) Settings.GREEN_TEXT_COLOR else Color.WHITE
      val fontScale = FontHelper.topPanelInfoFont.data.scaleY
      FontHelper.topPanelInfoFont.data.setScale(fontScale.times(.825f))
      FontHelper.renderFontLeftTopAligned(
        sb,
        FontHelper.topPanelInfoFont,
        display(),
        x,
        y,
        c.cpy()
      )
      FontHelper.topPanelInfoFont.data.setScale(fontScale)
    }
  }
}