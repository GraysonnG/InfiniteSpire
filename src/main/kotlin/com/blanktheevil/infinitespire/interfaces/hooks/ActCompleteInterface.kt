package com.blanktheevil.infinitespire.interfaces.hooks

interface ActCompleteInterface : IInfiniteSpire {
  fun onActCompleted(actId: String)

  companion object {
    val subscribers = mutableListOf<ActCompleteInterface>()
  }
}