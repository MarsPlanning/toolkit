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
import com.meowbase.ui.widget.style.IconStyle


/*
 * author: 凛
 * date: 2020/8/8 3:12 AM
 * github: https://github.com/RinOrz
 * description: 定义图标控件的通用样式
 */
class Icons(
  /** 小图标样式 */
  small: IconStyle = IconStyle(size = 12.dp),
  /** 通常的图标样式 */
  medium: IconStyle = IconStyle(size = 24.dp),
  /** 较大的图标样式 */
  large: IconStyle = IconStyle(size = 44.dp),
  /** 显示在按钮内的图标样式 */
  button: IconStyle = IconStyle(size = 16.dp),
) {
  /**
   * 需要拷贝一份新的图标样式副本并修改 [Style.id]
   * 以主题系统分辨其他地方的某个控件使用的图标样式是否为这里的
   */

  val small = small.new(0)
  val medium = medium.new(1)
  val large = large.new(2)
  val button = button.new(3)

  companion object {
    /**
     * 当应用图标样式时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断图标样式备份是否存在
     * 如果存在，根据图标样式备份的 [Style.id] 判断图标样式是否为主题图标样式库中的
     *
     * NOTE: 当 [Style.id] 不为 0 时既代表这不是一个主题样式，不需要更新
     * @return 最后返回主题更新后的图标样式库的实际图标样式对象
     */
    internal fun IconStyle.resolveIcon(ui: Ui): IconStyle = ui.currentIcons.run {
      when (id) {
        /** 重新获取一遍即可达到更新效果，因为 [currentIcons] 值其实已经变化了 */
        0 -> small
        1 -> medium
        2 -> large
        3 -> button
        else -> this@resolveIcon // 并非为主题库中的图标样式，不需要更新
      }
    }
  }
}


/** 返回当前 Ui 主题范围中的图标样式库 */
@PublishedApi internal inline val Ui.currentIcons: Icons get() = currentTheme.icons