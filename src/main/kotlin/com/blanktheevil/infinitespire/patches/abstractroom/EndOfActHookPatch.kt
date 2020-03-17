package com.blanktheevil.infinitespire.patches.abstractroom

import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.interfaces.OnActComplete
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss

@Suppress("unused")
@SpirePatch(clz = AbstractRoom::class, method = "update")
class EndOfActHookPatch {
  companion object {
    @JvmStatic
    @SpirePostfixPatch
    fun handleEndOfAct(room: AbstractRoom) {
      if (room is MonsterRoomBoss && room.phase == AbstractRoom.RoomPhase.COMPLETE) {
        log.info("Executing end of act hook...")
        OnActComplete.items.forEach {
          it.onActCompleted(AbstractDungeon.id)
        }
      }
    }
  }
}