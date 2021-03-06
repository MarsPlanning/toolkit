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

package com.meowbase.toolkit.coroutines

/**
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2021/02/08 - 13:49
 */
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * 将流上游的操作切换到主线程中执行
 *
 * NOTE: 此调度程序只能用于与界面交互和执行快速工作
 * 例如调用 suspend 函数、运行 Android 界面框架操作，以及更新 LiveData 对象等
 */
fun <T> Flow<T>.flowOnUI(): Flow<T> = flowOn(Dispatchers.Main)

/**
 * 将流上游的操作切换到 Default 线程中执行
 *
 * NOTE: 适合在主线程之外执行占用大量 CPU 资源的工作，例如对列表排序和解析 JSON 等
 */
fun <T> Flow<T>.flowOnDefault(): Flow<T> = flowOn(Dispatchers.Default)

/**
 * 将流上游的操作切换到 IO 线程中执行
 *
 * NOTE: 适合在主线程之外执行磁盘或网络 I/O, 例如从文件中读取数据或向文件中写入数据，以及运行任何网络操作等
 */
fun <T> Flow<T>.flowOnIO(): Flow<T> = flowOn(Dispatchers.IO)