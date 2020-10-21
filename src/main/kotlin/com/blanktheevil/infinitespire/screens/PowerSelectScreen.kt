package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.ui.buttons.PowerButton
import com.blanktheevil.infinitespire.vfx.DarkBgEffect
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.powers.AbstractPower

class PowerSelectScreen : Screen<PowerSelectScreen>() {
  companion object {
    private val strings = languagePack.getUIString("PowerSelectScreen".makeID())
  }

  var powerButtons = List(8) { PowerButton(0f, 0f) }

  var selectedPower: AbstractPower? = null
  private val darkBgEffect = DarkBgEffect(this)

  override fun updateScreen() {
    updateHitboxes()

    if (show) {
      if (this.selectedPower != null || powerButtons.isNullOrEmpty()) {
        close()
      }
    }

    darkBgEffect.update()
  }

  private fun updateHitboxes() {
    if (!show) return

    var index = 0
    for (y in 0 until 2) {
      for (x in 0 until 4) {
        powerButtons[index++].updateButton(x, y)
      }
    }
  }

  override fun renderScreen(sb: SpriteBatch) {
    darkBgEffect.render(sb)

    if (!show) return

    FontHelper.renderFontCentered(
      sb,
      FontHelper.buttonLabelFont,
      strings.TEXT[0],
      Settings.WIDTH.div(2f),
      Settings.HEIGHT.div(3f).times(2f),
      Color.WHITE.cpy()
    )
    powerButtons.forEach {
      it.render(sb)
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
    this.powerButtons = powerList.asSequence().map {
      PowerButton(0f, 0f, power = it) { power ->
        selectedPower = power
        callback.invoke(this)
      }
    }.toList()
    this.callback = callback
    selectedPower = null
    show = true
    AbstractDungeon.isScreenUp = show
  }
}