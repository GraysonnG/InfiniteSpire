package com.blanktheevil.infinitespire.monsters

import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class Doppleganger {
  companion object {
    private fun getRandomType(): CharacterType {
      return when (AbstractDungeon.monsterRng.random(3)) {
        0 -> CharacterType.IRONCLAD
        1 -> CharacterType.SILENT
        2 -> CharacterType.DEFECT
        else -> CharacterType.WATCHER
      }
    }
  }

  enum class CharacterType {
    IRONCLAD,
    SILENT,
    DEFECT,
    WATCHER
  }


}