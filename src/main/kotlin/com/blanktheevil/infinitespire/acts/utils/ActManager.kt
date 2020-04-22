package com.blanktheevil.infinitespire.acts.utils

import actlikeit.dungeons.CustomDungeon
import basemod.BaseMod
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.acts.TheVoid
import com.blanktheevil.infinitespire.extensions.connectToNode
import com.blanktheevil.infinitespire.monsters.Nightmare
import com.blanktheevil.infinitespire.monsters.Voidling
import com.blanktheevil.infinitespire.monsters.utils.Encounters
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.monsters.MonsterInfo
import com.megacrit.cardcrawl.monsters.city.Healer
import com.megacrit.cardcrawl.rooms.AbstractRoom

object ActManager {
  fun init() {
    CustomDungeon.addAct(-1, TheVoid())
    registerMonsters()
    registerEncounters()
  }

  fun registerMonsters() {
    BaseMod.addMonster(Encounters.VOIDLING, BaseMod.GetMonster { Voidling() })
    BaseMod.addMonster(Encounters.VOIDLING_MYSTIC, BaseMod.GetMonsterGroup {
      MonsterGroup(arrayOf(
        Voidling(-200f),
        Healer(120f, 0f)
      ))
    })
    BaseMod.addMonster(Encounters.THREE_VOIDLINGS, BaseMod.GetMonsterGroup {
      MonsterGroup(arrayOf(
        Voidling(-255f),
        Voidling(0f, -25f),
        Voidling(255f)
      ))
    })
    BaseMod.addMonster(Encounters.NIGHTMARE_BOSS, BaseMod.GetMonster { Nightmare() })
  }

  private fun registerEncounters() {
    BaseMod.addMonsterEncounter(TheVoid.ID, MonsterInfo(Encounters.VOIDLING, 0.5f))
    BaseMod.addMonsterEncounter(TheVoid.ID, MonsterInfo(Encounters.VOIDLING_MYSTIC, 1f))
    BaseMod.addMonsterEncounter(TheVoid.ID, MonsterInfo(Encounters.THREE_VOIDLINGS, 1f))

    BaseMod.addBoss(
      TheVoid.ID,
      Encounters.NIGHTMARE_BOSS,
      Textures.ui.getString("map/bossIcon.png", true),
      Textures.ui.getString("map/bossIcon-outline.png", true)
    )
  }

  fun makeRowWithCenteredRoom(floorNum: Int, room: AbstractRoom): ArrayList<MapRoomNode> =
    makeRowWithRoomInCol(floorNum, 3, room)

  fun makeRandomRowWithRoom(floorNum: Int, room: AbstractRoom): ArrayList<MapRoomNode> {
    val rand = AbstractDungeon.mapRng.random(2, 4)
    return makeRowWithRoomInCol(floorNum, rand, room)
  }

  private fun makeRowWithRoomInCol(floorNum: Int, colNum: Int, room: AbstractRoom): ArrayList<MapRoomNode> =
    List(7) { index ->
      MapRoomNode(index, floorNum).apply {
        if (index == colNum) this.room = room
      }
    } as ArrayList<MapRoomNode>

  fun connectNonEmptyNodes(map: List<List<MapRoomNode>>) {
    var lastNode: MapRoomNode? = null
    map.forEach { row ->
      row.forEach { node ->
        if (node.room != null) {
          if (lastNode != null) {
            lastNode?.connectToNode(node)
          }
          lastNode = node
        }
      }
    }
  }
}