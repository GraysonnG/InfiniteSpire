package com.blanktheevil.infinitespire.patches.abstractroom

import com.blanktheevil.infinitespire.utils.log
import com.blanktheevil.infinitespire.interfaces.hooks.ActCompleteInterface
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss

@Suppress("unused")
@SpirePatch(clz = AbstractRoom::class, method = "update")
object EndOfActHookPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun handleEndOfAct(room: AbstractRoom) {
    if (room is MonsterRoomBoss && room.phase == AbstractRoom.RoomPhase.COMPLETE) {
      log.info("Executing end of act hook...")
      ActCompleteInterface.subscribers.forEach {
        it.onActCompleted(AbstractDungeon.id)
      }
    }
  }
}