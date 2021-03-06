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

@file:Suppress("ClassName", "ViewConstructor", "FunctionName", "MemberVisibilityCanBePrivate")

package com.meowbase.ui.extension.list.implement

import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.R
import com.meowbase.ui.Ui
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.extension.list.RecyclableList
import com.meowbase.ui.extension.list.implement.data.DataSource
import com.meowbase.ui.widget.implement.FakeLayoutScope


/** Item 类型唯一值 */
internal typealias ItemType = Int

/** 数据类型名称 */
internal typealias DataType = String

/*
 * author: 凛
 * date: 2020/8/20 5:05 PM
 * github: https://github.com/RinOrz
 * description: Item 定义域，用于创建视图、绑定视图
 */
@UiKitMarker
abstract class ItemDefinition(parent: ViewGroup) : FakeLayoutScope(parent), Ui {
  protected var content: View? = null

  protected var binder: ItemBinder<*, *>? = null

  /** 当前定义中的视图持有者缓存 */
  private var _viewHolder: ViewHolder? = null

  /** 获取当前定义中的视图持有者缓存 */
  val viewHolder
    get() = _viewHolder
      ?: ViewHolder(getItemView(), binder).also { _viewHolder = it }

  /**
   * 这是一个假 [ViewGroup]，所以无需重写 [onLayout]
   * 实际上被添加的所有子视图都会添加到 [RecyclableList]
   * 用处是可以在区域内调用其他 [Ui] 小部件
   */
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  /** 获取当前定义中的视图 */
  fun getItemView() = content ?: error(CreateTips)

  /** 避免多次添加视图到 [content] */
  fun checkContentAdded() = require(content == null) {
    "多次添加 Item 视图, [itemContent] 只能被设置一次！每个 Item 只能拥有一个 View/ViewGroup"
  }

  override fun addView(child: View?, index: Int, params: LayoutParams?) {
    checkContentAdded()
    child?.setTag(R.id.real_parent_view, realParent)
    content = child
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    checkContentAdded()
    child?.setTag(R.id.real_parent_view, realParent)
    content = child
    return true
  }

  companion object {
    internal const val CreateTips = "Bind 前请先设置一个且只能设置一个 Item 视图到 ItemDefinition*.itemContent！"
  }
}


/**
 * Item 定义域
 * [S] Item 数据的类型（用于区分数据源中不同类型的数据）
 * [V] Item 视图类型
 * @see ItemDefinition2
 */
class ItemDefinition1<S, V> @PublishedApi internal constructor(parent: ViewGroup) :
  ItemDefinition(parent) {
  /**
   * [V] 类型的 Item 视图
   * @see ListDefineScope.DefineItem
   * @see ItemDefinition2.itemContent
   */
  @Suppress("UNCHECKED_CAST")
  val itemContent: V
    get() = getItemView() as V

  /**
   * 绑定当前数据类型为 [S] 的 Item 视图
   * 由于 [RecyclableList] 回收机制，所以 Item 与数据绑定操作必须要在 [bindScope] 内进行
   *
   * @see bindScope 一个提供了当前 Item 的数据的代码块
   */
  fun Bind(bindScope: ItemBinder<S, V>.(S) -> Unit) =
    ItemBinder(itemContent, bindBlock = bindScope).also { binder = it }

  /**
   * 绑定当前数据类型为 [S] 的 Item 视图
   * 由于 [RecyclableList] 回收机制，所以 Item 与数据绑定操作必须要在 [bindScope] 内进行
   *
   * @see bindScope 一个提供了当前 Item 在 [DataSource] 中的位置和当前 Item 的数据的代码块
   */
  fun Bind(bindScope: ItemBinder<S, V>.(index: Int, item: S) -> Unit) =
    ItemBinder(itemContent, bindIndexedBlock = bindScope).also { binder = it }
}


/**
 * 为不同类型 [S] 的 Item 定义一个区域
 * @see ItemDefinition1 与其不同的是，[ItemDefinition2] 绑定的视图类型一律按照 [View] 处理
 * @see content
 */
class ItemDefinition2<S> @PublishedApi internal constructor(parent: ViewGroup) :
  ItemDefinition(parent) {
  /**
   * Item 视图
   * @see ListDefineScope.DefineItem
   * @see ItemDefinition1.itemContent
   */
  val itemContent: View get() = getItemView()

  /**
   * 绑定当前数据类型为 [S] 的 Item 视图
   * 由于 [RecyclableList] 回收机制，所以 Item 与数据绑定操作必须要在 [bindScope] 内进行
   *
   * @see bindScope 一个提供了当前 Item 的数据的代码块
   */
  fun Bind(bindScope: ItemBinder<S, View>.(S) -> Unit) =
    ItemBinder(itemContent, bindBlock = bindScope).also { binder = it }

  /**
   * 绑定当前数据类型为 [S] 的 Item 视图
   * 由于 [RecyclableList] 回收机制，所以 Item 与数据绑定操作必须要在 [bindScope] 内进行
   *
   * @see bindScope 一个提供了当前 Item 在 [DataSource] 中的位置和当前 Item 的数据的代码块
   */
  fun Bind(bindScope: ItemBinder<S, View>.(index: Int, item: S) -> Unit) =
    ItemBinder(itemContent, bindIndexedBlock = bindScope).also { binder = it }
}