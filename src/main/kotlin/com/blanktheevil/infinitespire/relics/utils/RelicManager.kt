package com.blanktheevil.infinitespire.relics.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.relics.Relic
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.unlock.UnlockTracker

@AutoAdd.Ignore
object RelicManager {
  fun addAllRelics() {
    AutoAdd(InfiniteSpire.modid)
      .packageFilter(Relic::class.java)
      .filter(NotPackageFilter(RelicManager::class.java))
      .any(Relic::class.java) { info, relic ->
        InfiniteSpire.logger.info("Added Relic: ${relic.relicId}")
        RelicLibrary.add(relic)
        if (info.seen) {
          UnlockTracker.markRelicAsSeen(relic.relicId)
        }
      }
  }

  fun getRelicList(amount: Int): List<AbstractRelic> {
    fun getUniqueRelic(list: List<AbstractRelic>, depth: Int = 0): AbstractRelic {
      var relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS)
      list.forEach {
        if (depth < 100 && it.relicId == relic.relicId) {
          relic = getUniqueRelic(list, depth + 1)
        }
      }
      return relic
    }

    val relics = mutableListOf<AbstractRelic>()
    for (i in 0 until amount) {
      relics.add(getUniqueRelic(relics))
    }

    return relics
  }
}