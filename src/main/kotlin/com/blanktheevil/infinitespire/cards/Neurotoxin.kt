package com.blanktheevil.infinitespire.cards

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction
import com.megacrit.cardcrawl.powers.PoisonPower

class Neurotoxin : Card(BUILDER) {
  companion object {
    val ID = "Neurotoxin".makeID()
    private const val POISON_CREEP = 2
    private const val POISON_CREEP_UPG = 3
    private const val MAGIC = 1
    private val BUILDER = CardBuilder(ID)
      .img("neurotoxin")
      .cost(1)
      .skill()
      .enemy()
      .green()
      .rare()
      .exhaust()
      .init {
        misc = MAGIC
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
        setPoison(this, POISON_CREEP)
      }
      .upgr {
        setPoison(this, POISON_CREEP_UPG)
        initializeDescription()
      }
      .use { player, monster ->
        addToBot(IncreaseMiscAction(this.uuid, misc, getPoison(this)))
        monster?.applyPower(
          PoisonPower(monster, player, misc),
          source = player!!
        )
      }

    private fun setPoison(card: Card, amount: Int) {
      if (card is Neurotoxin) {
        card.poisonamt = amount
      }
    }

    private fun getPoison(card: Card): Int {
      return if (card is Neurotoxin) {
        card.poisonamt
      } else {
        0
      }
    }
  }

  var poisonamt = 0

  override fun applyPowers() {
    baseMagicNumber = misc
    magicNumber = misc
    super.applyPowers()
    initializeDescription()
  }
}