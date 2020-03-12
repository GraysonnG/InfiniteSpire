package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.Gdx
import com.blanktheevil.infinitespire.InfiniteSpire
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JSONHelper {
  companion object {
    fun <T> readFileAsMapOf(fileString: String, typeToken: TypeToken<T>): T {
      InfiniteSpire.logger.info("loadJson: ${typeToken.type.typeName}")
      return Gson().fromJson(
        Gdx.files.internal(fileString).readString(),
        typeToken.type
      )
    }
  }
}