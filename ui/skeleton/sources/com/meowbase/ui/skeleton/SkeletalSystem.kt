/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("ViewConstructor", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")

package com.meowbase.ui.skeleton

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import com.meowbase.toolkit.data.matchParent
import com.meowbase.toolkit.view.backgroundColor
import com.meowbase.toolkit.widget.LayoutParams
import java.util.*

/**
 * 骨骼系统
 * 我们将所有的界面认为是一个屏幕骨架或骨骼
 * 每一个骨架部分中还可以有无数个小骨骼
 *
 * 结构
 * ```
 * DecorView
 * - Content
 *   - SkeletalSystem
 *     - Skeleton1
 *       - SubSkeleton1
 *       - SubSkeleton2
 *         - ..
 *       - ...
 *     - Skeleton2
 *     - Skeleton3
 *     - ....
 * ```
 *
 * @author 凛
 * @date 2020/9/23 下午2:06
 * @github https://github.com/RinOrz
 * @param activity 需要注册骨骼系统的活动
 * @param main 主界面
 */
class SkeletalSystem(
  val activity: Activity,
  val main: Skeleton,
) : FrameLayout(activity) {
  val stackManager = StackManager()

  init {
    id = Id
    tag = Tag
    backgroundColor = Color.BLACK
    layoutParams = LayoutParams(matchParent, matchParent)

    main.system = this
    main.activityOrNull = activity
    // 将主界面推到栈顶
    stackManager.push(main)
  }

  fun dispatchCreate(savedInstanceState: Bundle?) {
    stackManager.current.performCreate(savedInstanceState)
  }

  fun dispatchStart() {
    stackManager.current.onAppear()
  }

  fun dispatchStop() {
    stackManager.current.onDisappear()
  }

  fun dispatchDestroy() {
    stackManager.current.onDestroy()
  }

  fun dispatchSaveInstanceState(outState: Bundle) {
    stackManager.current.onSaveInstanceState(outState)
  }

  /**
   * 分发与拦截返回事件
   * @return true 为拦截（由 [Skeleton] 自己处理）
   * false 为不拦截（由 [Activity] 处理）
   */
  fun interceptBackPressed(): Boolean {
    return if (stackManager.isOnlyOne) {
      // 当栈只有一个 Skeleton 时，不要拦截返回事件，交给 Activity 来处理
      false
    } else {
      // 拥有多于一个 Skeleton 时，拦截返回事件，由 Skeleton 自己处理
      stackManager.current.onBackPressed()
      true
    }
  }

  fun findSkeletonView(id: UUID) {
  }

  companion object {
    const val Tag = "SkeletalSystem"
    val Id = generateViewId()
  }
}

/**
 * 创建并绑定一个骨骼系统到 [Activity]
 *
 * @receiver 屏幕附加到的活动
 * @param mainSkeleton 请求一个当前活动的主骨架
 */
inline fun Activity.skeletalSystem(mainSkeleton: () -> Skeleton): Lazy<SkeletalSystem> {
  val skeleton = mainSkeleton()
  return lazy { SkeletalSystem(this, skeleton) }
}

/**
 * 创建并绑定一个骨骼系统到 [Activity]
 *
 * @receiver 屏幕附加到的活动
 * @param mainSkeleton 请求一个当前活动的主骨架
 */
inline fun Activity.skeletalSystem(mainSkeleton: Skeleton): Lazy<SkeletalSystem> =
  lazy { SkeletalSystem(this, mainSkeleton) }