package com.blanktheevil.infinitespire.patches.avhari.room

import com.blanktheevil.infinitespire.rooms.utils.NodeVFXManager
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.map.MapRoomNode

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = MapRoomNode::class, method = "updateEmerald")
object AvhariRoomUpdatePatch {
  @JvmStatic
  @SpirePrefixPatch
  fun update(node: MapRoomNode) {
    NodeVFXManager.update(node)
  }
}