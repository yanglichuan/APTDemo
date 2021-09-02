package com.charlie.plugin.visitor;


import com.charlie.plugin.data.Warehouse;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
@Deprecated
class RouteMethodVisitor extends MethodVisitor {
        RouteMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }
        @Override
        public void visitInsn(int opcode) {
            //generate code before return
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {

                for (String s : Warehouse.INSTANCE.getList()) {
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitLdcInsn(s);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            Warehouse.TargetClassName,
                            Warehouse.TargetInvokeMethod,
                            Warehouse.ASMString, false);
                }

//
//                extension.classList.each { name ->
//                    name = name.replaceAll("/", ".")
//                    mv.visitLdcInsn(name)//类名
//                    // generate invoke register method into LogisticsCenter.loadRouterMap()
//                    mv.visitMethodInsn(Opcodes.INVOKESTATIC
//                            , ScanSetting.GENERATE_TO_CLASS_NAME
//                            , ScanSetting.REGISTER_METHOD_NAME
//                            , "(Ljava/lang/String;)V"
//                            , false)
//                }
            }
            super.visitInsn(opcode);
        }
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals);
        }
    }