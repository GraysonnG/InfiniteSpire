package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet

class CubicDiamond : Relic(ID, IMG, TIER, SOUND){
  companion object {
    val ID = "Cubic Diamond".makeID()
    private const val IMG = "cubicdiamond"
    private val TIER = RelicTier.SPECIAL
    private val SOUND = LandingSound.CLINK
  }

  override fun onEquip() {
    val relicToReplaceIndex = player.relics.indexOfFirst { it.tier == RelicTier.STARTER }
    player.relics[relicToReplaceIndex].onUnequip()
    getUniqueStarterRelic(player.relics[relicToReplaceIndex]).apply {
      instantObtain(player, 0, true)
      playLandingSFX()
      flash()
    }
  }

  private fun getUniqueStarterRelic(original: AbstractRelic): AbstractRelic {
    val relicToAdd = RelicLibrary.starterList.getRandomItem(AbstractDungeon.relicRng) ?: Circlet()
    return if (relicToAdd.relicId == original.relicId) {
      getUniqueStarterRelic(original)
    } else {
      relicToAdd
    }
  }
}