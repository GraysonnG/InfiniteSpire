package com.blanktheevil.infinitespire.patches.neowevent

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.quests.utils.QuestManager
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.neow.NeowEvent

@Suppress("unused", "unused_parameter")
@SpirePatch(clz = NeowEvent::class, method = SpirePatch.CONSTRUCTOR, paramtypez = [Boolean::class])
object ConstructorPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun infiniteSetup(event: NeowEvent) {
    log.info("Adding 5 Quests to Quest Log...")
    for (i in 0 until 5) {
      InfiniteSpire.questLog.add(
        QuestManager.getRandomQuest()
      )
    }
  }
}