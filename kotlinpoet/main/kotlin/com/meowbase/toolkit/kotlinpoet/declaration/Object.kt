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

package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的 Object
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun buildObjectType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.objectBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的 Object
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun buildObjectType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.objectBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个 Object 并添加到 [FileDeclaration]
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun FileDeclaration.objectType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.objectBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个 Object 并添加到 [FileDeclaration]
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun FileDeclaration.objectType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.objectBuilder(name)).also(declare).get()

