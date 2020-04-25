package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.defect.ChannelAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.orbs.AbstractOrb

@Suppress("unused")
class Oblivion : BlackCard(BUILDER) {
  companion object {
    val ID = "Oblivion".makeID()
    private const val MAGIC = 2
    private const val DAMAGE = 13

    private val BUILDER = CardBuilder(id = ID)
      .img("oblivion.png")
      .cost(1)
      .attack()
      .enemy()
      .use { p, m ->
        addToBot(
          DamageAction(
            m,
            DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
          )
        )
        for (i in 0 until magicNumber) {
          addToBot(
            ChannelAction(
              AbstractOrb.getRandomOrb(true)
            )
          )
        }
      }
      .init {
        this.baseDamage = DAMAGE
        this.magicNumber = MAGIC
        this.baseMagicNumber = MAGIC
      }
      .upgr {
        this.upgradeMagicNumber(2)
      }
  }
}