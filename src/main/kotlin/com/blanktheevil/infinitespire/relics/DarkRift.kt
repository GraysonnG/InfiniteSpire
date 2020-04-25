package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.vfx.BlackCardVfx
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction

class DarkRift : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "DarkRift".makeID()
    private const val IMG = "darkRift"
    private val TIER = RelicTier.STARTER
    private val SOUND = LandingSound.MAGICAL
  }

  override fun atBattleStart() {
    flash()
    addToBot(VFXAction(BlackCardVfx()))
    addToBot(RelicAboveCreatureAction(player, this))
    addToBot(
      MakeTempCardInDrawPileAction(
        CardManager.getRandomBlackCard(),
        1,
        true,
        true
      )
    )
  }
}