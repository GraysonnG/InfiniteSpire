package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.inBottleSoul
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.Config
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class BottledSoul : InfiniteSpireRelic(ID, IMG, TIER, SOUND), Savable {
  companion object {
    val ID = "Bottled Soul".makeID()
    private val IMG = "bottledsoul-cracked"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  var selectedCard: AbstractCard? = null
  var cardSelected: Boolean = false

  init {
    InfiniteSpire.subscribe(this)
  }


  // GAHHH FUCK THIS IM NOT SURE WHAT TO DO

  override fun beforeConfigSave() {
    if (cardSelected && selectedCard != null) {
      InfiniteSpire.config.bottledSoul.selectedCardID = selectedCard!!.cardID
    }
  }

  override fun afterConfigLoad(config: Config) {
    val soulSave = config.bottledSoul
    if (soulSave.selectedCardID != "") {
      AbstractDungeon.player.masterDeck.group.forEach {
        if (it.cardID == soulSave.selectedCardID) {
          cardSelected = true
          selectedCard = it
          it.inBottleSoul = true
        }
      }
    }
  }

  override fun clearData() {
    InfiniteSpire.config.bottledSoul.selectedCardID = ""
  }
}