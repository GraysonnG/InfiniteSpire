package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.relics.abstracts.CrystalRelic
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction

class ShieldingShard : CrystalRelic(ID, IMG) {
  companion object {
    val ID = "ShieldingShard".makeID()
    private const val IMG = "healingshard"
  }

  override fun onActCompleted(actId: String) {
    this.counter += 3
  }

  override fun atBattleStart() {
    actionManager.addToBottom(
      AddTemporaryHPAction(player, player, counter)
    )
  }
}