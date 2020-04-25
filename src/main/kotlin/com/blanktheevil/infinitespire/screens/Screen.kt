package com.blanktheevil.infinitespire.screens

import basemod.IUIElement
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

abstract class Screen<T> : SpireElement {
  var show = false
  var callback: (screen: T) -> Unit = {}
  protected val uiElements = mutableListOf<IUIElement>()

  override fun update() {
    if (!show) return
    uiElements.forEach {
      it.update()
    }
    if (!show) return
    updateScreen()
    AbstractDungeon.isScreenUp = show
  }

  override fun render(sb: SpriteBatch) {
    if (!show) return
    renderScreen(sb)
    uiElements.forEach {
      it.render(sb)
    }
  }

  protected abstract fun renderScreen(sb: SpriteBatch)
  protected abstract fun updateScreen()
  protected abstract fun open(callback: (screen: T) -> Unit = {})

  abstract fun close()
}