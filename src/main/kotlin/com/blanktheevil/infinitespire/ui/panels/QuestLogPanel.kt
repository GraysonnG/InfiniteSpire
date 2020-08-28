package com.blanktheevil.infinitespire.ui.panels

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.clamp
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.quests.Quest
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.helpers.input.InputHelper

class QuestLogPanel : SpireElement {
  companion object {
    private val FEATURE_TOGGLE = Settings.isDebug // game has to start in debug mode for this to be true
    private val questIcon: TextureAtlas.AtlasRegion by lazy {
      Textures.ui.get("questlog2/quest-icon.png").asAtlasRegion()
    }
    private val collapseIcon: TextureAtlas.AtlasRegion by lazy {
      ImageMaster.CF_RIGHT_ARROW.asAtlasRegion()
    }
    private const val X_POS = 30f
    private const val Y_POS = 282f
    private const val QUEST_ICON_OFFSET = 52f
    private const val X_SPACING = 52f
    private const val TIP_Y_OFFSET = 48f
    private val WHITE = Color(1f, 1f, 1f, 1f)
  }

  private var open = true
  private var toggleInterp = 0f
  private var rotation = 0f
  private var opacity = 1f

  private val collapseButtonHb = Hitbox(
    X_POS.scale(),
    Settings.HEIGHT.minus(Y_POS.scale()),
    collapseIcon.packedWidth.toFloat(),
    collapseIcon.packedHeight.toFloat()
  )

  fun toggle() {
    open = !open
  }

  override fun update() {
    if (FEATURE_TOGGLE) return
    collapseButtonHb.update()
    InfiniteSpire.questLog.forEachIndexed { i, quest ->
      quest.hb.update(
        X_POS.scale()
          .plus(X_SPACING.scale().times(i).times(opacity))
          .plus(QUEST_ICON_OFFSET.scale().times(opacity)),
        Settings.HEIGHT.minus(Y_POS.scale())
      )
      quest.hb.update()
    }

    if (!open)
      toggleInterp += deltaTime.times(5f)
    else
      toggleInterp -= deltaTime.times(5f)

    toggleInterp = toggleInterp.clamp(0f, 1f)
    rotation = Interpolation.fade.apply(90f, 0f, toggleInterp)
    opacity = Interpolation.fade.apply(1f, 0f, toggleInterp)

    if (collapseButtonHb.hovered && InputHelper.justClickedLeft) toggle()
  }

  override fun render(sb: SpriteBatch) {
    if (FEATURE_TOGGLE) return
    renderQuests(sb)
    sb.color = WHITE.cpy()
    sb.draw(
      collapseIcon,
      X_POS.scale(),
      Settings.HEIGHT.minus(Y_POS.scale()),
      collapseIcon.packedWidth.div(2f),
      collapseIcon.packedHeight.div(2f),
      collapseIcon.packedWidth.toFloat(),
      collapseIcon.packedHeight.toFloat(),
      scale,
      scale,
      rotation,
      true
    )
    collapseButtonHb.render(sb)
  }

  private fun renderQuests(sb: SpriteBatch) {
    InfiniteSpire.questLog.forEach { quest ->
      sb.color = quest.color.cpy().also { it.a = opacity }
      sb.draw(
        questIcon,
        quest.hb.x,
        quest.hb.y,
        questIcon.packedWidth.div(2f),
        questIcon.packedHeight.div(2f),
        questIcon.packedWidth.toFloat(),
        questIcon.packedHeight.toFloat(),
        scale,
        scale,
        0f
      )
      sb.color = WHITE.cpy().also { it.a = opacity }
      sb.draw(
        quest.img,
        quest.hb.x,
        quest.hb.y,
        questIcon.packedWidth.div(2f),
        questIcon.packedHeight.div(2f),
        questIcon.packedWidth.toFloat(),
        questIcon.packedHeight.toFloat(),
        scale,
        scale,
        0f
      )
      renderTip(sb, quest)
      quest.hb.render(sb)
    }
  }

  private fun renderTip(sb: SpriteBatch, quest: Quest) {
    if (quest.hb.hovered && open) {
      TipHelper.renderGenericTip(
        quest.hb.x,
        quest.hb.y.minus(TIP_Y_OFFSET.scale()),
        quest.name,
        quest.makeDescription()
      )
    }
  }
}