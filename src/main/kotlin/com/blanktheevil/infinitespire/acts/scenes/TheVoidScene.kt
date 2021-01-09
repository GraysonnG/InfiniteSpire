package com.blanktheevil.infinitespire.acts.scenes

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.dungeons.Exordium
import com.megacrit.cardcrawl.scenes.AbstractScene
import com.megacrit.cardcrawl.screens.DeathScreen
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect

class TheVoidScene : AbstractScene(ATLAS_URL) {
  init {
    ambianceName = "AMBIANCE_BEYOND"
    fadeInAmbiance()
  }

  companion object {
    private val ATLAS_URL = Textures.acts.getString("thevoid/scene.atlas", true)
  }

  val particles = mutableListOf<DeathScreenFloatyEffect>()

  override fun renderCombatRoomBg(sb: SpriteBatch) {
    renderAtlasRegionIf(sb, this.bg, true)

    particles.forEach { it.render(sb) }
  }

  override fun update() {
    super.update()

    if (particles.size < 50) {
      particles.add(DeathScreenFloatyEffect())
    }

    particles.forEach { it.update() }
    particles.removeIf { it.isDone }
  }

  override fun renderCombatRoomFg(sb: SpriteBatch) {
  }

  override fun renderCampfireRoom(sb: SpriteBatch) {
    renderAtlasRegionIf(sb, this.campfireBg, true)
  }

  override fun randomizeScene() {
  }
}