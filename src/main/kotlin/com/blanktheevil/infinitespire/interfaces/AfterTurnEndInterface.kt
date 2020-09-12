package com.blanktheevil.infinitespire.interfaces

interface AfterTurnEndInterface {
  fun afterEndTurn()

  companion object {
    val subscribers = mutableListOf<AfterTurnEndInterface>()
  }
}