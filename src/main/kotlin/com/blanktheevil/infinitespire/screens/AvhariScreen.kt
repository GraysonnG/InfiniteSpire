package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class AvhariScreen : Screen<AvhariScreen>() {
  override fun renderScreen(sb: SpriteBatch) {
  }

  override fun updateScreen() {
  }

  override fun open(callback: (screen: AvhariScreen) -> Unit) {
    this.callback = callback
  }

  override fun close() {
  }
}