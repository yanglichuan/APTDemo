package com.charlie.plugin.visitor;

import com.charlie.plugin.data.Warehouse;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TargetMethodClassVisitor extends ClassVisitor {

       public TargetMethodClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        public void visit(int version, int access, String name, String signature,
                          String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
        }
        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            //generate code into this method
            if (name.equals(Warehouse.TargetMethod)) {
                mv = new InvokeAdviceAdapter( mv, access, name, desc);
            }
            return mv;
        }
    }


