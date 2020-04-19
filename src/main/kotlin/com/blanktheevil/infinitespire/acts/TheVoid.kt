package com.blanktheevil.infinitespire.acts

import actlikeit.dungeons.CustomDungeon
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.acts.scenes.TheVoidScene
import com.blanktheevil.infinitespire.acts.utils.ActManager
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.map.MapGenerator
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.rooms.MonsterRoom
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.rooms.ShopRoom
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import com.megacrit.cardcrawl.scenes.AbstractScene

@Suppress("unused")
class TheVoid : CustomDungeon {
  constructor() :
      super(NAME, ID)

  constructor(cd: CustomDungeon, p: AbstractPlayer, emptyList: ArrayList<String>) :
      super(cd, p, emptyList)

  constructor(cd: CustomDungeon, p: AbstractPlayer, saveFile: SaveFile) :
      super(cd, p, saveFile)

  init {
  }

  companion object {
    val ID = "TheVoid".makeID()
    private val strings = InfiniteSpire.actStringsKt[ID]
    val NAME = strings!!.NAME
  }

  override fun makeMap() {
    AbstractDungeon.map = ArrayList<ArrayList<MapRoomNode>>().apply {
      add(ActManager.makeRowWithCenteredRoom(0, MonsterRoom()))
      add(ActManager.makeRowWithCenteredRoom(1, RestRoom()))
      add(ActManager.makeRowWithCenteredRoom(2, ShopRoom()))
      add(ActManager.makeRowWithCenteredRoom(3, MonsterRoomBoss()))
    }
    ActManager.connectNonEmptyNodes(map)

    log.info("Generated the following dungeon map:")
    log.info(MapGenerator.toString(map, true))
    log.info("Game Seed: " + Settings.seed)

    AbstractDungeon.firstRoomChosen = false
    AbstractDungeon.fadeIn()
  }

  override fun generateMonsters() {
    AbstractDungeon.eliteMonsterList = ArrayList()
    AbstractDungeon.eliteMonsterList.also {
      it.add("Shield and Spear")
      it.add("Shield and Spear")
      it.add("Shield and Spear")
    }
  }

  override fun initializeBoss() {
    AbstractDungeon.bossList.also {
      it.add("The Heart")
      it.add("The Heart")
      it.add("The Heart")
    }
  }

  override fun DungeonScene(): AbstractScene = TheVoidScene()
}