package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.powers.AbstractPower

class CriticalPower(player: AbstractPlayer) : Power(
  player,
  -1,
  BUILDER
) {
  companion object {
    val powerID = "CriticalPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .img("crit.png")
      .buff()
  }

  override fun updateDesc() {
    this.description = strings.DESCRIPTIONS[0]
  }

  override fun atDamageFinalGive(damage: Float, type: DamageInfo.DamageType?): Float =
    if (type == DamageInfo.DamageType.NORMAL) damage.times(2f) else damage

  override fun onAfterUseCard(card: AbstractCard?, action: UseCardAction?) {
    if (card != null && card.type == AbstractCard.CardType.ATTACK) {
      addToTop(
        RemoveSpecificPowerAction(owner, owner, powerID)
      )
    }
  }

  override fun makeCopy(): AbstractPower {
    return CriticalPower(this.owner as AbstractPlayer)
  }
}