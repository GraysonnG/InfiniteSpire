package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower

class SuperSlowPower(
  owner: AbstractMonster,
  amount: Int
) : Power(owner, amount, BUILDER) {
  companion object {
    val powerID = "ShreddedPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .debuff()
      .img("superslow.png")
  }

  override fun updateDesc() {
    this.description = StringBuilder(strings.DESCRIPTIONS[0])
      .append(owner.name)
      .append(strings.DESCRIPTIONS[1])
      .append(amount.times(10))
      .append(strings.DESCRIPTIONS[2])
      .toString()
  }

  override fun makeCopy(): AbstractPower = SuperSlowPower(owner as AbstractMonster, amount)

  override fun onAfterUseCard(card: AbstractCard?, action: UseCardAction?) {
    owner.applyPower(
      SuperSlowPower(owner as AbstractMonster, 1)
    )
  }

  override fun atDamageReceive(damage: Float, damageType: DamageInfo.DamageType?): Float {
    return if (damageType != null && damageType == DamageInfo.DamageType.NORMAL) {
      damage * (1.0f + amount * 0.1f)
    } else {
      damage
    }
  }
}