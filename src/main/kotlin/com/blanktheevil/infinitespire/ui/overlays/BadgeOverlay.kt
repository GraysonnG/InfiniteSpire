package com.blanktheevil.infinitespire.ui.overlays

import basemod.interfaces.OnStartBattleSubscriber
import basemod.interfaces.PostBattleSubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.rooms.AbstractRoom

class BadgeOverlay : SpireElement, OnStartBattleSubscriber, PostBattleSubscriber {

  private val badgesToDisplay = mutableListOf<Badge>()

  fun addBadge(badge: Badge) {
    if (!badgesToDisplay.any { it.id == badge.id }) {
      badge.life = 3f.plus(0.25f.times(badgesToDisplay.size))
      badgesToDisplay.add(badge)
    }
  }

  override fun update() {
    badgesToDisplay.forEach {
      it.life -= deltaTime
    }
    badgesToDisplay.removeIf { it.life < 0f }
  }

  override fun render(sb: SpriteBatch) {
    badgesToDisplay.forEachIndexed { index, badge ->
      FontHelper.renderFontCentered(
        sb,
        FontHelper.topPanelInfoFont,
        badge.name,
        Settings.WIDTH.div(2f),
        Settings.HEIGHT.div(2f).minus(index * 30f.scale()),
        Color.WHITE.cpy()
      )
    }
  }

  override fun receiveOnBattleStart(p0: AbstractRoom?) {
    badgesToDisplay.clear()
  }

  override fun receivePostBattle(p0: AbstractRoom?) {
    badgesToDisplay.clear()
  }
}