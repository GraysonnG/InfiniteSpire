package com.blanktheevil.infinitespire.patches.utils.filters

import basemod.AutoAdd
import org.clapper.util.classutil.ClassFinder
import org.clapper.util.classutil.ClassInfo

class NotPackageFilter(clz : Class<*>) : AutoAdd.PackageFilter(clz) {
  override fun accept(classInfo: ClassInfo?, classFinder: ClassFinder?): Boolean {
    return !super.accept(classInfo, classFinder)
  }
}