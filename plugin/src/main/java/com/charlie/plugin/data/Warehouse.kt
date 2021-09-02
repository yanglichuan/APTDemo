package com.charlie.plugin.data

import com.charlie.plugin.utils.LogUtil
import jdk.internal.org.objectweb.asm.util.ASMifiable
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import java.io.File
import java.util.*

/**
 * @author (ylc)
 * @datetime 2021-09-02 21:04 GMT+8
 * @email yanglichuan@ushareit.com
 * @detail :
 */
object Warehouse {

    const val ParsingOptions = ClassReader.EXPAND_FRAMES
    const val ASM_V = Opcodes.ASM7
    const val ClassSuffixes = ".class";
    const val BuildFile = "BuildConfig"
    const val InitMethod = "<init>"
    const val FromPackage = "com/ushareit/login/apt";
    const val TargetClass = "com/example/basecore/LoginManager.class"
    const val TargetClassName = "com/example/basecore/LoginManager"
    const val TargetName = "LoginManager"
    const val TargetInvokeMethod = "asmCall"
    const val ASMString = "(Ljava/lang/String;)V"
    const val TargetMethod = "initEngine"
    const val TransformName = "TransformLogin"
    const val isIncremental:Boolean = false

    const val TargetAnnotationClass = "DILoginIn_"
    const val TargetAnnotationMethod = "getClassNames"

    const val TreeSetPath = "java/util/TreeSet"

    const val LOG_FILE_PRINT_DIR = "~/"


    fun log(log:String){
        LogUtil.e("ylc", log)
    }

    fun haveData(): Boolean {
      return  listEnginePrivider.size > 0
    }

    fun getList(): TreeSet<String> {
        return  listEnginePrivider
    }

    fun add(name:String){
        listEnginePrivider.add(name)
    }
    var listEnginePrivider = TreeSet<String>()
    var jarfile: File? = null
}