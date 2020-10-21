package com.blanktheevil.infinitespire.models

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.relics.BottledMercury
import com.blanktheevil.infinitespire.relics.BottledSoul
import com.blanktheevil.infinitespire.utils.log
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

class SaveData(
  var voidShards: VoidShardCurrency = VoidShardCurrency(),
  var bottledSoul: BottledRelicData<BottledSoul> = BottledRelicData(),
  var bottledMercury: BottledRelicData<BottledMercury> = BottledRelicData(),
  var nightmareData: NightmareData = NightmareData(),
  var stats: Statistics = Statistics(),
  var settings: Settings = Settings(
    true,
    true,
    true,
    false
  ),
  var actNumber: Int = 0
) {
  fun clear() {
    log.info("Clearing Data...")
    bottledSoul = BottledRelicData()
    bottledMercury = BottledRelicData()
    nightmareData = NightmareData()
    voidShards = VoidShardCurrency(if (settings.shouldRetainShards) voidShards.count else 0)
    actNumber = 0
  }

  fun save() {
    log.info("Saving Data...")
    val file = File(dirPath)
    file.writer(UTF_8).also {
      it.write(Gson().toJson(this))
      it.close()
    }
  }

  companion object {
    val dirPath = ConfigUtils.CONFIG_DIR + File.separator + InfiniteSpire.modid + File.separator + "saveData.json"

    fun init(): SaveData {
      val file = File(dirPath)
      if (!file.exists()) {
        log.info("Creating Save File...")
        file.parentFile.mkdirs()
        file.createNewFile()
        return SaveData().also {
          it.save()
        }
      }

      return load()
    }

    fun load(): SaveData {
      log.info("Loading Data...")
      val file = File(dirPath)

      return try {
        Gson().fromJson(file.readText(UTF_8), SaveData::class.java)
      } catch (e: Exception) {
        log.error("Could not load data! Using Defaults instead!")
        SaveData()
      }
    }
  }
}