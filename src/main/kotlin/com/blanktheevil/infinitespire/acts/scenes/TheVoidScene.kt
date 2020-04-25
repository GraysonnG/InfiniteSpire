package com.blanktheevil.infinitespire.acts.scenes

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.scenes.AbstractScene

class TheVoidScene : AbstractScene(ATLAS_URL) {
  init {
    ambianceName = "AMBIANCE_BEYOND"
    fadeInAmbiance()
  }

  companion object {
    private val ATLAS_URL = Textures.acts.getString("thevoid/scene.atlas", true)
  }


  override fun renderCombatRoomBg(sb: SpriteBatch) {
    renderAtlasRegionIf(sb, this.bg, true)
  }

  override fun renderCombatRoomFg(sb: SpriteBatch) {
  }

  override fun renderCampfireRoom(sb: SpriteBatch) {
    renderAtlasRegionIf(sb, this.campfireBg, true)
  }

  override fun randomizeScene() {
  }
}