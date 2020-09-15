@file:Suppress("NAME_SHADOWING", "OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.mars.toolkit.view.*
import com.mars.ui.core.Modifier
import com.mars.ui.foundation.SafeArea

/**
 * 将视图的整体放置到安全的区域中
 *
 * @param top 竖屏状态下，避免 ui 内容入侵状态栏区域
 * @param bottom 竖屏状态下，避免 ui 内容入侵到导航栏区域
 * @param left 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @param right 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @see View.padding
 * @see SafeArea
 */
fun Modifier.safeArea(
  top: Boolean = true,
  bottom: Boolean = true,
  left: Boolean = true,
  right: Boolean = true,
) = +WindowInsetsModifier(top, bottom, left, right, SafeAreaApplyMode.Margin)

/**
 * 将视图的内容（子视图）放置到安全的区域中
 *
 * @param top 竖屏状态下，避免 ui 内容入侵状态栏区域
 * @param bottom 竖屏状态下，避免 ui 内容入侵到导航栏区域
 * @param left 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @param right 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @see View.margin
 * @see SafeArea
 */
fun Modifier.safeContentArea(
  top: Boolean = true,
  bottom: Boolean = true,
  left: Boolean = true,
  right: Boolean = true,
) = +WindowInsetsModifier(top, bottom, left, right, SafeAreaApplyMode.Padding)


/** 限制视图在安全区域的具体实现 */
private class WindowInsetsModifier(
  val top: Boolean,
  val bottom: Boolean,
  val left: Boolean,
  val right: Boolean,
  val mode: SafeAreaApplyMode,
) : Modifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    val paddingLeft = myself.paddingLeft
    val paddingTop = myself.paddingTop
    val paddingRight = myself.paddingRight
    val paddingBottom = myself.paddingBottom

    val marginLeft = myself.marginLeft
    val marginTop = myself.marginTop
    val marginRight = myself.marginRight
    val marginBottom = myself.marginBottom
    ViewCompat.setOnApplyWindowInsetsListener(myself) { myself, insets ->
      val top = if (top) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top else 0
      val left = if (left) insets.getInsets(WindowInsetsCompat.Type.systemBars()).left else 0
      val right = if (right) insets.getInsets(WindowInsetsCompat.Type.systemBars()).right else 0
      val bottom = if (bottom) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0
      when (mode) {
        SafeAreaApplyMode.Padding -> myself.updatePadding(
          left = paddingLeft + left,
          top = paddingTop + top,
          right = paddingRight + right,
          bottom = paddingBottom + bottom
        )
        SafeAreaApplyMode.Margin -> {
          myself.marginTop = marginTop + top
          myself.marginLeft = marginLeft + left
          myself.marginRight = marginRight + right
          myself.marginBottom = marginBottom + bottom
        }
      }
      insets
    }
  }
}

private enum class SafeAreaApplyMode { Padding, Margin }