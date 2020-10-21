package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.Input
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.input.InputAction

class DebugControls {

  private val toggleDebug = InputAction(Input.Keys.D)
  private val togglePause = InputAction(Input.Keys.P)
  val stepForward = InputAction(Input.Keys.PERIOD)
  private val shiftL = InputAction(Input.Keys.SHIFT_LEFT)
  private val shiftR = InputAction(Input.Keys.SHIFT_RIGHT)
  private var advanceTimer = 0f
  private var shouldBePaused = false

  fun update() {
    if (shiftDown) {
      debugControlsUpdate()
    }
  }

  private fun debugControlsUpdate() {
    if (toggleDebug.isJustPressed) {
      Settings.isDebug = !Settings.isDebug
    }

    if (shouldBePaused) {
      advanceTimer -= deltaTime
      if (advanceTimer < 0f) {
        advanceTimer = 0f
        InfiniteSpire.pauseGame = true
      }
    }

    if (stepForward.isJustPressed && advanceTimer <= 0) {
      if (!InfiniteSpire.pauseGame) {
        shouldBePaused = true
        InfiniteSpire.pauseGame = true
      } else {
        advanceTimer = deltaTime * 2f
        InfiniteSpire.pauseGame = false
      }
    }

    if (togglePause.isJustPressed && advanceTimer <= 0) {
      InfiniteSpire.pauseGame = !InfiniteSpire.pauseGame
      shouldBePaused = InfiniteSpire.pauseGame
    }
  }

  private val shiftDown: Boolean get() = shiftL.isPressed || shiftR.isPressed
}