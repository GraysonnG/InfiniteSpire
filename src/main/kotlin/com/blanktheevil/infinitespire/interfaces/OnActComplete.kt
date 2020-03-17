package com.blanktheevil.infinitespire.interfaces

interface OnActComplete : IInfiniteSpire {
  fun onActCompleted(actId: String)

  companion object {
    val items = mutableListOf<OnActComplete>()
  }
}