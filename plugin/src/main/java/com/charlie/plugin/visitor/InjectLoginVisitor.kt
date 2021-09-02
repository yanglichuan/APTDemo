package com.charlie.plugin.visitor;

import com.charlie.plugin.PluginVisitor
import com.charlie.plugin.utils.LogUtil
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter

class InjectLoginVisitor(
    var className: String?,
    mv:MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(org.objectweb.asm.Opcodes.ASM7, mv, access, name, descriptor) {

    override fun onMethodEnter() {
        super.onMethodEnter()
        if (name == "<init>") {
            //跳过构造
            return
        }
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        if (name == "<init>") {
            return
        }
        if(className!!.contains("DILoginIn_", true)
            && name.equals("getClassNames")){

            mv.visitTypeInsn(NEW, "java/util/TreeSet")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/TreeSet", "<init>", "()V", false)
            mv.visitVarInsn(ASTORE, 2)
            val label2 = Label()
            mv.visitLabel(label2)
            mv.visitLineNumber(21, label2)

            PluginVisitor.list.forEach {
                mv.visitVarInsn(ALOAD, 2)
                mv.visitLdcInsn(it)
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/TreeSet", "add", "(Ljava/lang/Object;)Z", false)
                mv.visitInsn(POP)
            }
            val label5 = Label()
            mv.visitLabel(label5)
            mv.visitVarInsn(ALOAD, 2)
            mv.visitInsn(POP)
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ARETURN);
            LogUtil.e("mmmm", "进去了 $className")
        }
    }
}
