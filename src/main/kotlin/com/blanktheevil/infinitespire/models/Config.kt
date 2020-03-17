package com.blanktheevil.infinitespire.models

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.relics.BottledMercury
import com.blanktheevil.infinitespire.relics.BottledSoul
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

class Config(
  var voidShards: VoidShardCurrency = VoidShardCurrency(),
  var bottledSoul: BottledRelicData<BottledSoul> = BottledRelicData(),
  var bottledMercury: BottledRelicData<BottledMercury> = BottledRelicData(),
  var questLog: QuestLog = QuestLog(),
  var shouldDoParticles: Boolean = true,
  var shouldSpawnLords: Boolean = true,
  var stats: Statistics = Statistics()
) {
  fun save() {
    val file = File(dirPath)
    file.writer(UTF_8).also {
      it.write(Gson().toJson(this))
      it.close()
    }
  }

  companion object {
    val dirPath = ConfigUtils.CONFIG_DIR + File.separator + InfiniteSpire.modid + File.separator + "config.json"

    fun init(): Config {
      val file = File(dirPath)
      if (!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
        return Config().also {
          it.save()
        }
      }

      return load()
    }

    fun load(): Config {
      val file = File(dirPath)

      return try {
        Gson().fromJson(file.readText(UTF_8), Config::class.java)
      } catch (e: Exception) {
        Config()
      }
    }
  }
}