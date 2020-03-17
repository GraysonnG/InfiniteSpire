package com.blanktheevil.infinitespire.cards

import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster

@Suppress("unused")
class Starlight : BlackCard(ID, IMG) {
  companion object {
    val ID = "Starlight".makeID()
    private val IMG = Textures.cards.getString("starlight.png")
    private const val MAGIC = 10
    private const val UPG_MAGIC = 5
  }

  init {
    this.baseMagicNumber = MAGIC
    this.magicNumber = MAGIC
    this.exhaust = true
  }

  override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
    addToBot(HealAction(p, p, this.magicNumber))
  }

  override fun onUpgrade() {
    upgradeMagicNumber(UPG_MAGIC)
  }
}