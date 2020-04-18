package com.blanktheevil.infinitespire.crossover

class Crossover(val modid: String, val addContent: () -> Unit = {}) {
  var isLoaded = false
}