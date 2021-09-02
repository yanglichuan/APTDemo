package com.charlie.plugin.visitor;

import com.charlie.plugin.data.Warehouse
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 这种方法有问题 应该在扫描之后 再去添加所有的string
 * 否则有可能不全
 * @property className String?
 * @constructor
 */
class InjectManagerVisitor(
    var className: String?,
    mv: MethodVisitor,
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
            if (className == Warehouse.TargetClassName && name.equals(Warehouse.TargetMethod)) {
                Warehouse.getList().forEach {
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitLdcInsn(it);
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        Warehouse.TargetClassName,
                        Warehouse.TargetInvokeMethod,
                        Warehouse.ASMString,
                        false
                    );
                }
            }
        }
    }
}
