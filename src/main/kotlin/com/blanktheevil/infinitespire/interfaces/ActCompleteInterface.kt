package com.blanktheevil.infinitespire.interfaces

interface ActCompleteInterface : IInfiniteSpire {
  fun onActCompleted(actId: String)

  companion object {
    val items = mutableListOf<ActCompleteInterface>()
  }
}