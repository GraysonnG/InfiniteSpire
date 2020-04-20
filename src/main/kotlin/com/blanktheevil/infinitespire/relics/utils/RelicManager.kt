package com.blanktheevil.infinitespire.relics.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.relics.Relic
import com.megacrit.cardcrawl.helpers.RelicLibrary
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
}