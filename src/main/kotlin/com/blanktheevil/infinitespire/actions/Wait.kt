package com.blanktheevil.infinitespire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction

class Wait(duration: Float) : AbstractGameAction() {
  init {
    this.setValues(null, null, 0)
    this.duration = duration
    this.actionType = ActionType.WAIT
  }

  override fun update() {
    this.tickDuration()
  }
}