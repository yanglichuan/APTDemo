package com.charlie.plugin.visitor;

import com.charlie.plugin.data.Warehouse
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 暂时不用这种方案
 * @property className String?
 * @constructor
 */
class AnnotationAdviceAdapter(
    var className: String?,
    mv:MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(Warehouse.ASM_V, mv, access, name, descriptor) {

    override fun onMethodEnter() {
        super.onMethodEnter()
        if (name == Warehouse.InitMethod) {
            //跳过构造
            return
        }
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        if (name == Warehouse.InitMethod) {
            return
        }

        className?.apply {
            if(contains(Warehouse.TargetAnnotationClass, true)
                && name.equals(Warehouse.TargetAnnotationMethod)){

                mv.visitTypeInsn(NEW, Warehouse.TreeSetPath)
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, Warehouse.TreeSetPath, Warehouse.InitMethod, "()V", false)
                mv.visitVarInsn(ASTORE, 2)
                val label2 = Label()
                mv.visitLabel(label2)
                mv.visitLineNumber(21, label2)

                Warehouse.getList().forEach {
                    mv.visitVarInsn(ALOAD, 2)
                    mv.visitLdcInsn(it)
                    mv.visitMethodInsn(INVOKEVIRTUAL, Warehouse.TreeSetPath, "add", "(Ljava/lang/Object;)Z", false)
                    mv.visitInsn(POP)
                }
                val label5 = Label()
                mv.visitLabel(label5)
                mv.visitVarInsn(ALOAD, 2)
                mv.visitInsn(POP)
                mv.visitVarInsn(ALOAD, 2);
                mv.visitInsn(ARETURN);
                Warehouse.log( "进去了 $className")
            }
        }
    }
}
