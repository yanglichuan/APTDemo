package com.charlie.plugin

import com.charlie.plugin.data.Warehouse
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class PluginVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
    /**
     * 访问的类名字
     */
    private var className: String? = null

    /***
     * 保存类名
     * @param version Int
     * @param access Int
     * @param name String
     * @param signature String
     * @param superName String
     * @param interfaces Array<out String>
     */
    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }


    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        Warehouse.log("类名："+ className)
        className?.let {
//            val startsWith = it.startsWith(Warehouse.FromPackage, true)
            val startsWith = it.endsWith("Engine",true)
            if(startsWith){
                Warehouse.add(it.replace("/", "."))
            }
        }
        Warehouse.log("list = "+ Warehouse.listEnginePrivider.toString())
        var methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return methodVisitor
    }
}
