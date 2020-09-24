package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.Input
import com.blanktheevil.infinitespire.InfiniteSpire
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.input.InputAction

class DebugControls {

  private val toggleDebug = InputAction(Input.Keys.D)
  private val togglePause = InputAction(Input.Keys.P)
  val stepForward = InputAction(Input.Keys.PERIOD)
  private val shiftL = InputAction(Input.Keys.SHIFT_LEFT)
  private val shiftR = InputAction(Input.Keys.SHIFT_RIGHT)

  fun update() {
    if (shiftDown) {
      debugControlsUpdate()
    }
  }

  private fun debugControlsUpdate() {
    if (toggleDebug.isJustPressed) {
      Settings.isDebug = !Settings.isDebug
    }

    if (togglePause.isJustPressed) {
      InfiniteSpire.pauseGame = !InfiniteSpire.pauseGame
    }
  }

  private val shiftDown: Boolean get() = shiftL.isPressed || shiftR.isPressed
}