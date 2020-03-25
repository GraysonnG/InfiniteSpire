package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.inBottleSoul
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.models.SaveData
import com.megacrit.cardcrawl.cards.AbstractCard


class BottledSoul : BottleRelic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Bottled Soul".makeID()
    private val IMG = "bottledsoul-cracked"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun filterGridSelectBy(card: AbstractCard): Boolean = card.exhaust
  override fun isCardBottled(card: AbstractCard): Boolean = card.inBottleSoul

  override fun actionWhenUnEquipped(card: AbstractCard) {
    card.inBottleSoul = false
  }

  override fun actionWhenSelected(card: AbstractCard) {
    card.inBottleSoul = true
  }

  override fun beforeConfigSave(saveData: SaveData) {
    if (cardSelected && selectedCard != null) {
      saveData.bottledSoul.selectedCardID = selectedCard!!.cardID
    }
  }

  override fun afterConfigLoad(saveData: SaveData) {
    val soulSave = saveData.bottledSoul
    if (soulSave.selectedCardID != "") {
      player.masterDeck.group.forEach {
        if (it.cardID == soulSave.selectedCardID) {
          cardSelected = true
          selectedCard = it
          it.inBottleSoul = true
        }
      }
    }
  }

  override fun clearData(saveData: SaveData) {
    saveData.bottledSoul.selectedCardID = ""
  }
}