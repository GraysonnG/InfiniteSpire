package com.blanktheevil.infinitespire.rooms.avhari.interfaces

import com.blanktheevil.infinitespire.extensions.getVoidShardCount

abstract class ShopElementBase(protected val cost: Int) : ShopElement {
  protected var angle = 0f
  var purchaced = false
  var renderOnTop = false

  protected fun canAfford(): Boolean {
    return cost <= getVoidShardCount()
  }
}