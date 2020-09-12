package com.blanktheevil.infinitespire.badges.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.utils.log

@AutoAdd.Ignore
object BadgeManager {
  fun addAllBadges() {
    AutoAdd(InfiniteSpire.modid)
      .packageFilter(Badge::class.java)
      .filter(NotPackageFilter(BadgeManager::class.java))
      .any(Badge::class.java) { _, badge ->
        log.info("Added Badge: " + badge::class.java.simpleName)
        badge.subscribe()
      }
  }
}