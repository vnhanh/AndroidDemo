package com.vnhanh.common.log

import timber.log.Timber


/**
 * Custom Timber Debug Tree
 */
class AppDebugTree : Timber.DebugTree() {
    // Set format of timber tag as "fileName:lineNumber"
    override fun createStackElementTag(element: StackTraceElement): String = "${element.fileName}:${element.lineNumber}"
}
