package com.blanktheevil.infinitespire.models

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.relics.BottledMercury
import com.blanktheevil.infinitespire.relics.BottledSoul
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

class SaveData(
  var voidShards: VoidShardCurrency = VoidShardCurrency(),
  var bottledSoul: BottledRelicData<BottledSoul> = BottledRelicData(),
  var bottledMercury: BottledRelicData<BottledMercury> = BottledRelicData(),
  var nightmareData: NightmareData = NightmareData(),
  var questLog: QuestLog = QuestLog(),
  var shouldDoParticles: Boolean = true,
  var shouldSpawnLords: Boolean = true,
  var stats: Statistics = Statistics(),
  val shouldPromptEndless: Boolean = true
) {
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
        file.parentFile.mkdirs()
        file.createNewFile()
        return SaveData().also {
          it.save()
        }
      }

      return load()
    }

    fun load(): SaveData {
      val file = File(dirPath)

      return try {
        Gson().fromJson(file.readText(UTF_8), SaveData::class.java)
      } catch (e: Exception) {
        SaveData()
      }
    }
  }
}