package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.VenomPower
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ChangeStateAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class Voidling(xOffset: Float = 0.0f, yOffset: Float = 0.0f) : AbstractMonster(
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

  init {
    if (AbstractDungeon.ascensionLevel > 7)
      setHp(minHP.plus(ascHPOffset), maxHP.plus(ascHPOffset))
    else
      setHp(minHP, maxHP)


    if (AbstractDungeon.ascensionLevel > 2) {
      this.tackleDamage += 2
      this.venomShotAmt += 2
      this.fangAttack += 2
    }

    this.damage.also {
      it.add(DamageInfo(this, tackleDamage))
      it.add(DamageInfo(this, 1))
      it.add(DamageInfo(this, fangAttack))
    }

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
      else -> {}
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
        !lastMove(MoveBytes.VENOM_SHOT) -> setMove(MoveBytes.VENOM_SHOT, Intent.ATTACK_DEBUFF, damage[1].base)
        AbstractDungeon.ascensionLevel >= 17 -> setMove(MoveBytes.FANG_ATTACK, Intent.ATTACK_DEBUFF, damage[2].base, 2, true)
        else -> setMove(MoveBytes.TACKLE, Intent.ATTACK, damage[0].base)
      }
      moveInt < 45 -> setMove(MoveBytes.DEFEND, Intent.DEFEND)
      else -> setMove(MoveBytes.TACKLE, Intent.ATTACK, damage[0].base)
    }
  }

  override fun takeTurn() {
    when(nextMove) {
      MoveBytes.TACKLE -> {
        addToBot(
          ChangeStateAction(this, "ATTACK")
        )
        addToBot(
          DamageAction(player, damage[0], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
        )
      }
      MoveBytes.DEFEND -> {
        addToBot(
          GainBlockAction(this, this, blockAmt)
        )
      }
      MoveBytes.VENOM_SHOT -> {
        addToBot(
          DamageAction(player, damage[1], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
        )
        player.applyPower(
          VenomPower(player, this, venomShotAmt),
          source = this
        )
      }
      MoveBytes.FANG_ATTACK -> {
        for (i in 0 until 2) {
          addToBot(
            DamageAction(player, damage[2], AbstractGameAction.AttackEffect.SLASH_VERTICAL)
          )
          player.applyPower(
            VenomPower(player, this, fangPoison),
            source = this
          )
        }
      }
    }
    addToBot(RollMoveAction(this))
  }

  object MoveBytes {
    const val TACKLE: Byte = 0
    const val DEFEND: Byte = 1
    const val VENOM_SHOT: Byte = 2
    const val FANG_ATTACK: Byte = 3
  }
}