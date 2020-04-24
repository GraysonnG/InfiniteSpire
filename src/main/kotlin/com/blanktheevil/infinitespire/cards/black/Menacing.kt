package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.MenacingPower
import com.blanktheevil.infinitespire.vfx.MenacingVfx
import com.megacrit.cardcrawl.actions.animations.VFXAction

class Menacing : BlackCard(BUILDER) {
  companion object {
    val ID = "Menacing".makeID()
    private const val MAGIC = 1
    private const val UPGR_MAGIC = 1
    private val BUILDER = CardBuilder(ID)
      .img("menacing.png")
      .cost(1)
      .skill()
      .self()
      .exhaust()
      .init {
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        upgradeMagicNumber(UPGR_MAGIC)
        rawDescription = strings(ID).UPGRADE_DESCRIPTION
        initializeDescription()
      }
      .use { player, _ ->
        addToBot(VFXAction(MenacingVfx(), 0.5f))
        player?.applyPower(MenacingPower(player, magicNumber))
      }
  }
}