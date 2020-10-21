package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.monsters.utils.Move
import com.blanktheevil.infinitespire.monsters.utils.setMove
import com.blanktheevil.infinitespire.powers.VenomPower
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.log
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ChangeStateAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class Voidling(xOffset: Float = 0.0f, yOffset: Float = 0.0f) : Monster(
  strings.NAME,
  ID,
  32,
  0.0f,
  0.0f,
  200f,
  150f,
  null,
  xOffset,
  yOffset
) {
  companion object {
    val ID = "Voidling".makeID()
    val strings = languagePack.getMonsterStrings(ID)
  }

  private var tackleDamage = 17
  private var fangAttack = 9
  private var fangPoison = 2
  private var venomShotAmt = 7
  private var blockAmt = 16
  private val maxHP = 67
  private val minHP = 58
  private val ascHPOffset = 6

  private val tackleMove = Move(Intent.ATTACK, tackleDamage) {
    addToBot(
      ChangeStateAction(it, "ATTACK")
    )
    addToBot(
      DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
    )
  }
  private val defendMove = Move(Intent.DEFEND) {
    addToBot(GainBlockAction(it, it, blockAmt))
  }
  private val venomShotMove = Move(Intent.ATTACK_DEBUFF, 1) {
    log.info("MoveByte: ${getByte()}")
    addToBot(
      DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
    )
    player.applyPower(
      VenomPower(player, it, venomShotAmt),
      source = it
    )
  }
  private val fangAttackMove = Move(Intent.ATTACK_DEBUFF, fangAttack, multiplier = 2, isMultiDamage = true) {
    for (i in 0 until 2) {
      addToBot(
        DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.SLASH_VERTICAL)
      )
      player.applyPower(
        VenomPower(player, it, fangPoison),
        source = it
      )
    }
  }

  init {
    if (AbstractDungeon.ascensionLevel > 7)
      setHp(minHP.plus(ascHPOffset), maxHP.plus(ascHPOffset))
    else
      setHp(minHP, maxHP)


    if (AbstractDungeon.ascensionLevel > 2) {
      tackleMove.modify(damage = tackleDamage + 2)
      venomShotAmt += 2
      fangAttackMove.modify(damage = fangAttack + 2)
    }

    registerMove(tackleMove)
    registerMove(defendMove)
    registerMove(venomShotMove)
    registerMove(fangAttackMove)

    loadAnimation(
      Textures.monsters.getString("voidling/Voidling.atlas", true),
      Textures.monsters.getString("voidling/Voidling.json", true),
      1.0f
    )

    state.setAnimation(0, "idle", true).also {
      it.time = it.endTime.times(MathUtils.random())
    }
  }

  override fun changeState(stateName: String) {
    when (stateName) {
      "ATTACK" -> {
        state.setAnimation(0, "attack", false)
        state.addAnimation(0, "idle", true, 0.0f)
      }
      else -> doNothing()
    }
  }

  override fun damage(info: DamageInfo) {
    super.damage(info)
    if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
      state.setAnimation(0, "oof", false)
      state.addAnimation(0, "idle", true, 0.0f)
    }
  }

  override fun getMove(moveInt: Int) {
    when {
      moveInt < 30 || GameActionManager.turn == 0 -> when {
        !lastMove(venomShotMove.getByte()) -> setMove(venomShotMove)
        AbstractDungeon.ascensionLevel >= 17 -> setMove(fangAttackMove)
        else -> setMove(tackleMove)
      }
      moveInt < 45 -> setMove(defendMove)
      else -> setMove(tackleMove)
    }
  }
}