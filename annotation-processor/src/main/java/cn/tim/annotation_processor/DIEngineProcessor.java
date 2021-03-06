package cn.tim.annotation_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import cn.tim.annotation.DILoginEngine;

@AutoService(Processor.class)
public class DIEngineProvider extends AbstractProcessor {
    private Elements elementUtils;

    private static final String PlacePackage = "com.ushareit.login.apt";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(DILoginEngine.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("DIEngine Processor ~~~");

        Set<? extends Element> engines = roundEnv.getElementsAnnotatedWith(DILoginEngine.class);
        if (engines == null || engines.size() == 0) {
            return true;
        }
        try {
            for (Element engine : engines) {
                if (engine == null) {
                    return true;
                }

                /**
                 * 生成get方法
                 */
                MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("get")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.get(engine.asType()));

                /**
                 * 获得类名
                 */
                String className = ClassName.get(engine.asType()).toString();

                /**
                 * 添加get 函数内容
                 */
                bindViewMethodSpecBuilder.addCode("" +
                        "   try {\n" +
                        "      return (" + className + ")Class.forName(\"" + className + "\").newInstance();\n" +
                        "    } catch (Exception e) {\n" +
                        "      e.printStackTrace();\n" +
                        "    }\n" +
                        "    return null;\n");


                /**
                 * 创建类 Provider_ 开头的类
                 */
                TypeSpec typeSpec = TypeSpec.classBuilder("Provider_" + engine.getSimpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                  .addSuperinterface(ClassName.get("com.example.basecore","IGet"))
                        .addMethod(bindViewMethodSpecBuilder.build())
                        .build();
                JavaFile javaFile = JavaFile.builder(PlacePackage, typeSpec).build();

                javaFile.writeTo(processingEnv.getFiler());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}


//
//@DIProvider
//public final class Provider_GGEngine {
//    public static GGEngine get() {
//        try {
//            return (com.example.ggmodule.GGEngine)Class.forName("com.example.ggmodule.GGEngine").newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;}
//}
