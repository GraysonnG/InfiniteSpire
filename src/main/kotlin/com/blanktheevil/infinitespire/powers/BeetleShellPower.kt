package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.powers.AbstractPower

class BeetleShellPower(player: AbstractPlayer) : Power(
  player,
  1,
  BUILDER
) {
  companion object {
    val powerID = "BeetleShellPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .buff()
      .img("beetleshell.png")
  }

  override fun updateDesc() {
    this.description = strings.DESCRIPTIONS[0]
  }

  override fun modifyBlock(blockAmount: Float): Float = blockAmount.times(2f)

  override fun makeCopy(): AbstractPower = BeetleShellPower(this.owner as AbstractPlayer)
}