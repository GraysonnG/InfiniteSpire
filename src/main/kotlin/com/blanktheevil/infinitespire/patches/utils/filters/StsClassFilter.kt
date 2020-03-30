package com.blanktheevil.infinitespire.patches.utils.filters

import org.clapper.util.classutil.ClassFilter
import org.clapper.util.classutil.ClassFinder
import org.clapper.util.classutil.ClassInfo

class StsClassFilter(private val clz: Class<*>) : ClassFilter {
  override fun accept(classInfo: ClassInfo?, classFinder: ClassFinder?): Boolean {
    return if (classInfo != null) {
      val superClasses = mutableMapOf<String, ClassInfo>()
      classFinder?.findAllSuperClasses(classInfo, superClasses)
      superClasses.containsKey(clz.name)
    } else {
      false
    }
  }
}