package com.blanktheevil.infinitespire.cards

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction
import com.megacrit.cardcrawl.cards.DamageInfo

class OneForAll : Card(BUILDER) {
  companion object {
    val ID = "OneForAll".makeID()
    private const val MISC = 2
    private const val MAGIC = 2
    private val BUILDER = CardBuilder(id = ID)
      .img("oneforall.png")
      .uncommon()
      .red()
      .attack()
      .enemy()
      .cost(1)
      .use { p, m ->
        val effect = when {
          damage in 10..19 -> AbstractGameAction.AttackEffect.BLUNT_HEAVY
          damage >= 20 -> AbstractGameAction.AttackEffect.SMASH
          else -> AbstractGameAction.AttackEffect.BLUNT_LIGHT
        }

        addToBot(IncreaseMiscAction(this.uuid, this.misc, this.magicNumber))
        addToBot(DamageAction(
          m,
          DamageInfo(p, damage, damageTypeForTurn),
          effect
        ))
      }
  }

  init {
    this.misc = MISC
    this.baseMagicNumber = MAGIC
    this.magicNumber = MAGIC
    this.baseDamage = MISC
    this.exhaust = true
  }

  override fun onUpgrade() {
    upgradeMagicNumber(1)
  }

  override fun applyPowers() {
    this.baseDamage = this.misc
    super.applyPowers()
    this.initializeDescription()
  }
}