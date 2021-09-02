package com.charlie.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet

/**
 * created by charlie on 2021/6/30
 */
class TransformLogin : Transform() {

    override fun getName(): String {
        return "TransformLogin"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return ImmutableSet.of<QualifiedContent.ScopeType>(
            QualifiedContent.Scope.SUB_PROJECTS, QualifiedContent.Scope.PROJECT,
        )
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        if (transformInvocation != null) {
            if (!isIncremental) {
                //全量删掉所有
                transformInvocation.outputProvider.deleteAll()
            }
            //处理输入的文件夹和jars
            transformInvocation.inputs.forEach { transformInput ->
                println("handle directory ${transformInput.directoryInputs.size}")
                transformInput.directoryInputs.forEach {
                    TransformHelper.transformDirectory(it, transformInvocation.outputProvider, transformInvocation.isIncremental)
                }
                println("handle jars ${transformInput.jarInputs.size}")
                //jar暂时不处理，只是复制文件
                transformInput.jarInputs.forEach {
                    TransformHelper.transformJar(it, transformInvocation.outputProvider, transformInvocation.isIncremental)
                }
            }
        }
    }
}

