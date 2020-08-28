package com.blanktheevil.infinitespire.patches.avhari.room

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.rooms.utils.NodeVFXManager
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.map.MapRoomNode

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = MapRoomNode::class, method = "renderEmeraldVfx")
object AvhariNodeRenderPatch {
  @JvmStatic
  @SpirePrefixPatch
  fun renderVFX(node: MapRoomNode, sb: SpriteBatch) {
    NodeVFXManager.render(node, sb)
  }
}