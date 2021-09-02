package com.charlie.plugin

import com.charlie.plugin.utils.LogUtil
import com.charlie.plugin.utils.Utils
import com.charlie.plugin.visitor.InjectLoginVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import java.util.*

class PluginVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {

    companion object{
        var list = TreeSet<String>()
    }

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
        LogUtil.e("类名：", className)

        if(!className.isNullOrEmpty()){
            val startsWith = className!!.startsWith("com/ushareit/login/apt", true)
            if(startsWith){
                list.add(className!!.replace("/", "."))
            }
        }
        LogUtil.e("list = ", list.toString())


        var methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (className != null && !Utils.ignorePackageNames(className!!)) {
//            methodVisitor = DeleteLogVisitor(className, methodVisitor, access, name, descriptor)
            LogUtil.e("mkkkkkkkkmmm", className + name)
//            methodVisitor = MeasureMethodCostTimeVisitor(className, methodVisitor, access, name, descriptor)

            /**
             * 执行注入
             */
            methodVisitor = InjectLoginVisitor(className, methodVisitor, access, name, descriptor)

        }
        return methodVisitor
    }
}
