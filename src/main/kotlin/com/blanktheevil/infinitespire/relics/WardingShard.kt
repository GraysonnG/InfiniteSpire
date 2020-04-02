package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.relics.abstracts.CrystalRelic
import com.megacrit.cardcrawl.powers.DexterityPower

class WardingShard : CrystalRelic(ID, IMG) {
  companion object {
    val ID = "WardingShard".makeID()
    private const val IMG = "wardingshard"
  }

  override fun atBattleStart() {
    player.applyPower(
      DexterityPower(player, counter),
      amount = counter
    )
  }
}