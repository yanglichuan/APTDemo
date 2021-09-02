package com.charlie.plugin.visitor;

import com.charlie.plugin.PluginVisitor
import com.charlie.plugin.utils.LogUtil
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 方法耗时
 */
class InjectLoginVisitor(
    var className: String?,
    mv:MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(org.objectweb.asm.Opcodes.ASM7, mv, access, name, descriptor) {

//
//    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
////        println(" $opcode, $owner, $name, $descriptor, $isInterface")
//        if (Opcodes.ACC_STATIC.and(opcode) != 0 && owner == "android/util/Log" && (name == "d" || name == "i" || name == "e" || name == "w" || name == "v")
//            && descriptor == "(Ljava/lang/String;Ljava/lang/String;)I"
//        ) {
//            /**
//             * 直接 return 只是删除了 Log 指令的调用，但是对应的当前的操作数栈没有进行处理
//             */
//            return
//        }
//        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
//    }





    var startId = 0

    override fun onMethodEnter() {
        super.onMethodEnter()
        if (name == "<init>") {
            //跳过构造
            return
        }
//        println("onMethod Enter: $className, name = $name")
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        startId = newLocal(Type.LONG_TYPE)
//        mv.visitVarInsn(LSTORE, startId)
    }

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        if (name == "<init>") {
            return
        }
//
//        val label1 = Label()
//        mv.visitLabel(label1)
//        mv.visitLineNumber(22, label1)
//        mv.visitTypeInsn(NEW, "java/util/TreeSet")
//        mv.visitInsn(DUP)
//        mv.visitMethodInsn(INVOKESPECIAL, "java/util/TreeSet", "<init>", "()V", false)
//        mv.visitVarInsn(ASTORE, 2)
//        val label2 = Label()
//        mv.visitLabel(label2)
//        mv.visitLineNumber(23, label2)
//        mv.visitVarInsn(ALOAD, 2)
//        mv.visitVarInsn(ALOAD, 1)
//        mv.visitMethodInsn(
//            INVOKEVIRTUAL,
//            "java/util/TreeSet",
//            "addAll",
//            "(Ljava/util/Collection;)Z",
//            false
//        )
//        mv.visitInsn(POP)


        if(className!!.contains("DILoginIn_", true) && name.equals("getClassNames")){

//            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);

//        mv.visitVarInsn(Opcodes.ALOAD, 1)
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn.tim.apt_demo.BBE", "hi", "()V", false)



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
