package com.blanktheevil.infinitespire.acts

import actlikeit.dungeons.CustomDungeon
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.acts.scenes.TheVoidScene
import com.blanktheevil.infinitespire.acts.utils.ActManager
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.monsters.utils.Encounters
import com.blanktheevil.infinitespire.rooms.AvhariRoom
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.map.MapGenerator
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.rooms.MonsterRoom
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss
import com.megacrit.cardcrawl.rooms.MonsterRoomElite
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import com.megacrit.cardcrawl.scenes.AbstractScene

@Suppress("unused")
class TheVoid : CustomDungeon {
  constructor() :
      super(NAME, ID)

  constructor(cd: CustomDungeon, p: AbstractPlayer, emptyList: ArrayList<String>) :
      super(cd, p, emptyList)

  constructor(cd: CustomDungeon?, p: AbstractPlayer?, saveFile: SaveFile?) :
      super(cd, p, saveFile)

  companion object {
    val ID = "TheVoid".makeID()
    private val strings = InfiniteSpire.actStringsKt[ID]
    val NAME = strings!!.NAME
  }

  override fun getActNumberText(): String = strings!!.TEXT[0]

  override fun makeMap() {
    var floorNum = 0
    AbstractDungeon.map = ArrayList<ArrayList<MapRoomNode>>().also {
      it.add(ActManager.makeRowWithCenteredRoom(floorNum++, MonsterRoom()))
      it.add(ActManager.makeRandomRowWithRoom(floorNum++, AvhariRoom()))
      it.add(ActManager.makeRandomRowWithRoom(floorNum++, MonsterRoomElite()))
      it.add(ActManager.makeRandomRowWithRoom(floorNum++, RestRoom()))
      it.add(ActManager.makeRowWithCenteredRoom(floorNum++, MonsterRoomBoss()))
    }
    ActManager.connectNonEmptyNodes(map)

    log.info("Generated the following dungeon map:")
    log.info(MapGenerator.toString(map, true))
    log.info("Game Seed: " + Settings.seed)

    AbstractDungeon.firstRoomChosen = false
    AbstractDungeon.fadeIn()

  }

  override fun generateMonsters() {
    AbstractDungeon.monsterList = ArrayList()
    AbstractDungeon.monsterList.also {
      it.add(Encounters.THREE_VOIDLINGS)
      it.add(Encounters.VOIDLING)
      it.add(Encounters.VOIDLING_MYSTIC)
    }
    AbstractDungeon.eliteMonsterList = ArrayList()
    AbstractDungeon.eliteMonsterList.also {
      it.add(Encounters.CORRUPTED_SHAPES)
      it.add(Encounters.CORRUPTED_SHAPES)
      it.add(Encounters.CORRUPTED_SHAPES)
    }

    ActManager.registerMonsters()
  }



  override fun initializeBoss() {
    AbstractDungeon.bossList.also {
      it.add(Encounters.NIGHTMARE_BOSS)
      it.add(Encounters.NIGHTMARE_BOSS)
      it.add(Encounters.NIGHTMARE_BOSS)
    }
  }

  override fun DungeonScene(): AbstractScene = TheVoidScene()
}