package com.mars.ktcompiler

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.openapi.extensions.ExtensionPoint
import org.jetbrains.kotlin.com.intellij.openapi.extensions.Extensions
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.util.UserDataHolderBase
import org.jetbrains.kotlin.com.intellij.pom.PomModel
import org.jetbrains.kotlin.com.intellij.pom.PomModelAspect
import org.jetbrains.kotlin.com.intellij.pom.PomTransaction
import org.jetbrains.kotlin.com.intellij.pom.impl.PomTransactionBase
import org.jetbrains.kotlin.com.intellij.pom.tree.TreeAspect
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.TreeCopyHandler
import org.jetbrains.kotlin.config.CompilerConfiguration
import sun.reflect.ReflectionFactory


/**
 * 返回一个默认的 [Project]
 */
inline val defaultProject: Project get() = defaultProject()

/**
 * 返回一个默认的 [Project]
 * @param parentDisposable
 */
fun defaultProject(
  parentDisposable: Disposable = Disposer.newDisposable(),
  configuration: CompilerConfiguration = CompilerConfiguration(),
): Project {
  // https://github.com/JetBrains/kotlin/commit/2568804eaa2c8f6b10b735777218c81af62919c1
//  setIdeaIoUseFallback()

  configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

  val project = KotlinCoreEnvironment.createForProduction(
    parentDisposable,
    configuration,
    EnvironmentConfigFiles.JVM_CONFIG_FILES
  ).project as MockProject

  project.enableASTMutations()

  return project
}


/*
 * Enables AST mutations
 * https://github.com/pinterest/ktlint/blob/0ddc545ab0bcca08ac982bfc6de7a40cab789774/ktlint-core/src/main/kotlin/com/pinterest/ktlint/core/internal/KotlinPsiFileFactory.kt#L58
 */
private fun MockProject.enableASTMutations() {
  val extensionPoint = "org.jetbrains.kotlin.com.intellij.treeCopyHandler"
  val extensionClassName = TreeCopyHandler::class.java.name
  for (area in arrayOf(extensionArea, Extensions.getRootArea())) {
    if (!area.hasExtensionPoint(extensionPoint)) {
      area.registerExtensionPoint(extensionPoint, extensionClassName, ExtensionPoint.Kind.INTERFACE)
    }
  }

  registerService(PomModel::class.java, FormatPomModel())
}

private class FormatPomModel : UserDataHolderBase(), PomModel {

  override fun runTransaction(transaction: PomTransaction) {
    (transaction as PomTransactionBase).run()
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : PomModelAspect> getModelAspect(aspect: Class<T>): T? {
    if (aspect == TreeAspect::class.java) {
      // using approach described in https://git.io/vKQTo due to the magical bytecode of TreeAspect
      // (check constructor signature and compare it to the source)
      // (org.jetbrains.kotlin:kotlin-compiler-embeddable:1.0.3)
      val constructor = ReflectionFactory
        .getReflectionFactory()
        .newConstructorForSerialization(
          aspect,
          Any::class.java.getDeclaredConstructor(*arrayOfNulls<Class<*>>(0))
        )
      return constructor.newInstance() as T
    }
    return null
  }
}