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

package com.meowbase.ui.theme

import com.meowbase.ui.Ui
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.currentTheme
import com.meowbase.ui.widget.style.DividerStyle


/*
 * author: 凛
 * date: 2020/8/8 3:12 AM
 * github: https://github.com/RinOrz
 * description: 定义其他局部控件的一般通用样式
 */
class Styles(
  /** 分割线 */
  divider: DividerStyle = DividerStyle(thickness = 0.5.dp),
) {
  /**
   * 需要拷贝一份新的样式副本并修改 [Style.id]
   * 以主题系统分辨其他地方的某个控件使用的样式是否为这里的
   */

  val divider = divider.new(0)

  companion object {

    /**
     * 当应用样式时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断样式备份是否存在
     * 如果存在，根据样式备份的 [Style.id] 判断样式是否为主题样式库中的
     *
     * NOTE: 当 [Style.id] 不为 0 时既代表这不是一个主题样式，不需要更新
     * @return 最后返回主题更新后的样式库的实际样式对象
     */
    @Suppress("UNCHECKED_CAST")
    internal fun <R : Style<*>> R.resolveStyle(ui: Ui): R = when (this) {
      /** 重新获取一遍即可达到更新效果，因为 [currentStyles] 值其实已经变化了 */
      is DividerStyle -> if (id == 0) ui.currentStyles.divider else this
      else -> this
    } as R
  }
}

internal interface Style<T> {
  /** 用于主题系统分辨此样式是否是主题中的样式，并决定是否可更新 */
  var id: Int

  /** 创建一个样式副本并传入给定的 Id 值 */
  fun new(id: Int): T
}


/** 返回当前 Ui 主题范围中的样式库 */
@PublishedApi internal inline val Ui.currentStyles: Styles get() = currentTheme.styles