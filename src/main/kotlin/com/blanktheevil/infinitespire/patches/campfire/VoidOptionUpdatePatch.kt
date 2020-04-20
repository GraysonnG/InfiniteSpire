package com.blanktheevil.infinitespire.patches.campfire

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.rooms.CampfireUI

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = CampfireUI::class, method = "update")
object VoidOptionUpdatePatch {
  @JvmStatic
  @SpirePrefixPatch
  fun run(instance: CampfireUI) {
    InfiniteSpire.voidOption.updatePosition(instance)
    InfiniteSpire.voidOption.update()
  }
}