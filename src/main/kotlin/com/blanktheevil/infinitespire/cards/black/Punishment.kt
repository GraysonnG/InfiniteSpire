package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster

class Punishment : BlackCard(BUILDER) {
  companion object {
    val ID = "Punishment".makeID()
    private val BUILDER = CardBuilder(ID)
      .img("punishment.png")
      .cost(1)
      .attack()
      .enemy()
      .init {
        baseDamage = 0
      }
      .upgr {
        upgradeBaseCost(0)
      }
      .use { player, monster ->
        addToBot(
          DamageAction(monster, DamageInfo(player, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
        )
        rawDescription = strings(ID).UPGRADE_DESCRIPTION
        initializeDescription()
      }
  }

  private fun updateBaseDamage() {
    baseDamage = 0
    var newDamage = player.drawPile.size()
    newDamage += player.hand.size()
    newDamage += player.discardPile.size()

    baseDamage = newDamage.times(2)
    isDamageModified = true
    rawDescription = strings(ID).UPGRADE_DESCRIPTION
    initializeDescription()
  }

  override fun applyPowers() {
    updateBaseDamage()
    super.applyPowers()
  }

  override fun calculateCardDamage(mo: AbstractMonster?) {
    updateBaseDamage()
    super.calculateCardDamage(mo)
  }
}