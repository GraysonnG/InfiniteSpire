package com.blanktheevil.infinitespire.rooms.utils

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.rooms.AvhariRoom
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect

object NodeVFXManager {
  private val flames = ArrayList<FlameAnimationEffect>()
  private var flameVfxTimer = 0f

  fun render(node: MapRoomNode, sb: SpriteBatch) {
    if (node.room is AvhariRoom) {
      val scale = ReflectionHacks.getPrivate(
        node,
        MapRoomNode::class.java,
        "scale"
      ) as Float

      sb.color = Color.WHITE.cpy()
      flames.forEach {
        it.render(sb, scale)
      }
    }
  }

  fun update(node: MapRoomNode) {
    if (node.room is AvhariRoom) {
      flameVfxTimer -= deltaTime
      if (flameVfxTimer < 0f) {
        flameVfxTimer = MathUtils.random(0.2f, 0.4f)
        flames.add(FlameAnimationEffect(node.hb))
      }

      flames.removeIf {
        if (it.isDone) {
          it.dispose()
          it.isDone
        } else false
      }

      flames.forEach { it.update() }
    }
  }
}