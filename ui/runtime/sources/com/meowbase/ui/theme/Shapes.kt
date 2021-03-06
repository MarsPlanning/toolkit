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

@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.meowbase.ui.theme

import com.meowbase.ui.Ui
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.material.Material
import com.meowbase.ui.core.graphics.shape.RoundedCornerShape
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.shape.ZeroCornerSize
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.currentTheme

/*
 * author: 凛
 * date: 2020/8/8 3:31 AM
 * github: https://github.com/RinOrz
 * description: 定义一些通用的角形状
 * specification: https://material.io/design/shape/applying-shape-to-ui.html
 */
class Shapes(
  /** 小形状定义 */
  small: Shape = RoundedCornerShape(4.dp),
  /** 一般形状定义 */
  medium: Shape = RoundedCornerShape(4.dp),
  /** 用于大组件上的大形状定义*/
  large: Shape = RoundedCornerShape(ZeroCornerSize),
) {
  /**
   * 需要拷贝一份新的形状副本并修改 [Color.id]
   * 以主题系统分辨其他地方的某个控件使用的形状是否为这里的
   */

  val small: Shape = small.new(0)
  val medium: Shape = medium.new(1)
  val large: Shape = large.new(2)

  /**
   * 创建一份形状库的副本，以覆盖一些值
   */
  fun copy(
    small: Shape = this.small,
    medium: Shape = this.medium,
    large: Shape = this.large,
  ): Shapes = Shapes(small, medium, large)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Shapes

    if (small != other.small) return false
    if (medium != other.medium) return false
    if (large != other.large) return false

    return true
  }

  override fun hashCode(): Int {
    var result = small.hashCode()
    result = 31 * result + medium.hashCode()
    result = 31 * result + large.hashCode()
    return result
  }

  companion object {
    /**
     * 当应用形状时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断形状备份是否存在
     * 如果存在，根据形状备份的 [Material.id] 判断形状是否为主题排版库中的形状
     * @return 最后返回主题更新后的排版库的实际形状
     */
    internal fun Shape.resolveShape(ui: Ui): Shape = when (id) {
      /** 重新获取一遍即可达到更新效果，因为 [currentShapes] 值其实已经变化了 */
      0 -> ui.currentShapes.small
      1 -> ui.currentShapes.medium
      2 -> ui.currentShapes.large
      else -> this // 并非为主题库中的形状，不需要更新
    }
  }
}


/** 返回当前 Ui 主题范围中的形状库 */
@PublishedApi internal inline val Ui.currentShapes: Shapes get() = currentTheme.shapes
