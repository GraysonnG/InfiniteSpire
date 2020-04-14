package com.blanktheevil.infinitespire.utils

import com.badlogic.gdx.Gdx
import com.blanktheevil.infinitespire.extensions.log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JSONHelper {
  fun <T> readFileAsMapOf(fileString: String, typeToken: TypeToken<T>): T {
    log.info("loadJson: ${typeToken.type.typeName}")
    return Gson().fromJson(
      Gdx.files.internal(fileString).readString(),
      typeToken.type
    )
  }
}
