package com.blanktheevil.infinitespire.screens

import basemod.ModButton
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import java.util.function.Consumer

class InfiniteScreen : Screen<InfiniteScreen>() {
  init {
    uiElements.add(
      ModButton(Settings.WIDTH.div(2f), Settings.HEIGHT.div(2f), null, Consumer {})
    )
  }


  override fun open(callback: (screen: InfiniteScreen) -> Unit) {

  }

  override fun close() {

  }

  override fun updateScreen() {
  }

  override fun renderScreen(sb: SpriteBatch) {
  }
}