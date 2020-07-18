package com.blanktheevil.infinitespire.toppanel

import basemod.TopPanelItem
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.textures.Textures

@Deprecated("Remove Before Release")
class QuestLogButton : TopPanelItem(IMG, ID) {
  companion object {
    private val IMG = Textures.ui.get("topPanel/questLogIcon.png")
    val ID = "QuestLog".makeID()
  }

  override fun onClick() {
//    InfiniteSpire.questLogScreen.toggle()
  }
}