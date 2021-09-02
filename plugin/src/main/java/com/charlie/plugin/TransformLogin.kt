package com.charlie.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.charlie.plugin.data.Warehouse
import com.charlie.plugin.visitor.TargetMethodClassVisitor
import com.google.common.collect.ImmutableSet
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * created by charlie on 2021/6/30
 */
class TransformLogin : Transform() {

    override fun getName(): String {
        return Warehouse.TransformName
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
        return Warehouse.isIncremental
    }

    //refer hack class when object init
    private fun referHackWhenInit(inputStream: InputStream): ByteArray {
        var cr = ClassReader(inputStream)
        var cw = ClassWriter(cr, 0)
        var cv = TargetMethodClassVisitor(
            Warehouse.ASM_V,
            cw
        )
        cr.accept(cv, Warehouse.ParsingOptions)
        return cw.toByteArray()
    }

    private fun putNamesIntoJar() {
        if (Warehouse.haveData()) {
            if (Warehouse.jarfile?.name?.endsWith(".jar") == true) {
                if (Warehouse.jarfile != null) {
                    var jarFile = Warehouse.jarfile!!
                    var optJar = File(jarFile.parent, jarFile.name + ".opt")
                    if (optJar.exists())
                        optJar.delete()
                    var file = JarFile(jarFile)
                    var enumeration = file.entries()
                    var jarOutputStream = JarOutputStream(FileOutputStream(optJar))

                    while (enumeration.hasMoreElements()) {
                        var jarEntry = enumeration.nextElement() as JarEntry
                        var entryName = jarEntry.name
                        var zipEntry = ZipEntry(entryName)
                        var inputStream = file.getInputStream(jarEntry)
                        jarOutputStream.putNextEntry(zipEntry)
                        if (Warehouse.TargetClass == entryName) {
                            Warehouse.log("Insert init code to class >> " + entryName)
                            var bytes = referHackWhenInit(inputStream)
                            jarOutputStream.write(bytes)
                        } else {
                            jarOutputStream.write(IOUtils.toByteArray(inputStream))
                        }
                        inputStream.close()
                        jarOutputStream.closeEntry()
                    }
                    jarOutputStream.close()
                    file.close()

                    if (jarFile.exists()) {
                        jarFile.delete()
                    }
                    optJar.renameTo(jarFile)
                }
            }
        }
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
                    TransformHelper.transformDirectory(
                        it,
                        transformInvocation.outputProvider,
                        transformInvocation.isIncremental
                    )
                }
                println("handle jars ${transformInput.jarInputs.size}")
                //jar暂时不处理，只是复制文件
                transformInput.jarInputs.forEach {
                    TransformHelper.transformJar(
                        it,
                        transformInvocation.outputProvider,
                        transformInvocation.isIncremental
                    )
                }
            }
            putNamesIntoJar()

//            if (className != null && !Utils.ignorePackageNames(className!!)) {
////            methodVisitor = DeleteLogVisitor(className, methodVisitor, access, name, descriptor)
//                LogUtil.e("kkkk", className +"       " + name)
////            methodVisitor = MeasureMethodCostTimeVisitor(className, methodVisitor, access, name, descriptor)
//
//                /**
//                 * 执行注入
//                 */
//                methodVisitor = InjectLoginVisitor(className, methodVisitor, access, name, descriptor)
//                methodVisitor = InjectManagerVisitor(className, methodVisitor, access, name, descriptor)
//            }

        }
    }
}

