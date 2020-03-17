package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.powers.StrengthPower

class EmpoweringShard : CrystalRelic(relicID, IMG) {
  companion object {
    val relicID = "EmpoweringShard".makeID()
    private const val IMG = "empoweringshard"
  }

  override fun atBattleStart() {
    player.applyPower(
      StrengthPower(player, counter),
      amount = counter
    )
  }
}