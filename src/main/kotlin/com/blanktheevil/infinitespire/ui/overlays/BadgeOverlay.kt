package com.blanktheevil.infinitespire.ui.overlays

import basemod.interfaces.OnStartBattleSubscriber
import basemod.interfaces.PostBattleSubscriber
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.rooms.AbstractRoom

class BadgeOverlay : SpireElement, OnStartBattleSubscriber, PostBattleSubscriber {
  companion object {
    private const val DELAY = 0.1f
  }

  private val badgesToDisplay = mutableListOf<Badge>()
  private val badgesEnqueued = mutableListOf<Badge>()
  private var queueDelay = 0f

  fun addBadge(badge: Badge) {
    if (!badgesToDisplay.any { it.id == badge.id } && !badgesEnqueued.any { it.id == badge.id }) {
      badge.spawn(
        makePosVector(badgesEnqueued.size),
        3f.plus(0.25f.times(badgesEnqueued.size))
      )
      badgesEnqueued.add(badge)
    }
  }

  override fun update() {
    queueDelay -= deltaTime
    if (queueDelay <= 0f && badgesEnqueued.size > 0) {
      badgesToDisplay.add(badgesEnqueued.removeFirst())
      queueDelay = DELAY
    }
    badgesToDisplay.forEachIndexed { index, badge ->
      badge.updateTarget(makePosVector(index))
      badge.update()
    }
    badgesToDisplay.removeIf(Badge::isDead)
  }

  override fun render(sb: SpriteBatch) {
    badgesToDisplay.forEach { badge ->
      badge.render(sb)
    }
  }

  override fun receiveOnBattleStart(p0: AbstractRoom?) {
    badgesToDisplay.clear()
  }

  override fun receivePostBattle(p0: AbstractRoom?) {
    badgesToDisplay.clear()
  }

  private fun makePosVector(pos: Int) = Vector2(
    Settings.WIDTH.minus(45f.scale()),
    Settings.HEIGHT.div(2f).minus(30f.scale().times(pos))
  )
}