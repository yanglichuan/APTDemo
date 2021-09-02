package com.yxhuang.analytics.android.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AnalyticsClassVisitor extends ClassVisitor {

    private static final String TAG = "AnalyticsClassVisitor"

    private static final String SDK_API_CLASS = "com/yxhuang/asmlib/DataAutoTrackHelper"

    private ClassVisitor mClassVisitor
    private String[] mInterface

    private String className ;
    AnalyticsClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
        mClassVisitor = classVisitor
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        mInterface = interfaces
        className = name;
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        String nameDesc = name + descriptor

        println("------------AnalyticsClassVisitor name=" + name + " descriptor=" + descriptor + " nameDesc=" + nameDesc)


        LogUtil.e("cccc", className)

        if (className != null && !Utils.ignorePackageNames(className)) {
//            methodVisitor = DeleteLogVisitor(className, methodVisitor, access, name, descriptor)

            methodVisitor = new AnalyticsDefaultMethodVisitor(methodVisitor, access, name, descriptor) {

                // 这里插入指令
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    if (mInterface != null && mInterface.length > 0) {
//                    if (mInterface.contains('android/view/view$OnClickListener') && nameDesc == 'onClick(Landroid/view/View;)V' || descriptor == '(Landroid/view/View;)V'){

//                        methodVisitor.visitVisitor.visitVarInVarInsn(Opcodes.ALOAD, 1)
//                        visitMethodInsn(Opcodes.INVOKESTATIC, "cn.tim.apt_demo.BBE",
//                                'hi', '()V', false)
//                        methodVisitor.visitInsn(POP)
//                    }
                    }
                }
            }
        }
        return methodVisitor
    }
}