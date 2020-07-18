package com.blanktheevil.infinitespire.cards.black

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.dealDamage
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.textures.Textures
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.cards.DamageInfo

class SevenWalls : BlackCard(BUILDER) {
  companion object {
    val ID = "SevenWalls".makeID()
    private const val IMG = "walls.png"
    private const val IMG_2 = "spacetime.png"
    private const val BLOCK = 15
    private const val DAMAGE = 15
    private const val UPGR_BLOCK = 5
    private const val UPGR_DAMAGE = 5
    private val BUILDER = CardBuilder(ID)
      .img(IMG)
      .cost(2)
      .attack()
      .enemy()
      .init {
        baseDamage = DAMAGE
        baseBlock = BLOCK
      }
      .upgr {
        name = strings(ID).UPGRADE_DESCRIPTION
        ++timesUpgraded
        upgraded = true
        rawDescription = strings(ID).EXTENDED_DESCRIPTION[0]
        initializeDescription()
        initializeTitle()
        upgradeBlock(UPGR_BLOCK)
        upgradeDamage(UPGR_DAMAGE)

        textureImg = IMG_2
      }
      .use { player, monster ->
        addToBot(
          if (!upgraded) {
            GainBlockAction(player, player, this.block)
          } else {
            AddTemporaryHPAction(player, player, this.block)
          }
        )
        player.dealDamage(
          monster!!,
          damage,
          AbstractGameAction.AttackEffect.BLUNT_HEAVY
        )
      }
  }

  override fun render(sb: SpriteBatch) {
    if (upgraded) {
      textureImg = Textures.cards.getString(IMG_2)
      loadCardImage(textureImg)
    }
    super.render(sb)
  }
}