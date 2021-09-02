package com.charlie.plugin.visitor;

import com.charlie.plugin.data.Warehouse
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class InvokeAdviceAdapter(
    mv: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(org.objectweb.asm.Opcodes.ASM7, mv, access, name, descriptor) {

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

        Warehouse.getList().forEach {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(it);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                Warehouse.TargetClassName,
                Warehouse.TargetInvokeMethod,
                Warehouse.ASMString,
                false
            )
        }
    }
}
