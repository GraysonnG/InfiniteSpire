package com.blanktheevil.infinitespire.interfaces.hooks

interface AfterTurnEndInterface {
  fun afterEndTurn()

  companion object {
    val subscribers = mutableListOf<AfterTurnEndInterface>()
  }
}