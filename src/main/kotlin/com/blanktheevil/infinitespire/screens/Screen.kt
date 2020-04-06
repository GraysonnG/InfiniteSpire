package com.blanktheevil.infinitespire.screens

import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

abstract class Screen<T>: SpireElement {
  var show = false
  var callback: (screen: T) -> Unit = {}

  override fun update() {
    if (!show) return
    AbstractDungeon.isScreenUp = show
  }

  protected abstract fun open(callback: (screen: T) -> Unit = {})
  abstract fun close()
}