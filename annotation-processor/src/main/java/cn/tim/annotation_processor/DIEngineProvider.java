package cn.tim.annotation_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import cn.tim.annotation.DIEngine;
import cn.tim.annotation.DIProvider;

@AutoService(Processor.class)
public class DIEngineProvider extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(DIEngine.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("add DIEngine");

        Set<? extends Element> engines = roundEnv.getElementsAnnotatedWith(cn.tim.annotation.DIEngine.class);
        for (Element engine : engines) {
            if (engine == null) {
                return true;
            }

            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("get")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.get(engine.asType()));

            String className = ClassName.get(engine.asType()).toString();

            bindViewMethodSpecBuilder.addCode("" +
                    " try {\n" +
                    "      return (" + className + ")Class.forName(\"" + className + "\").newInstance();\n" +
                    "    } catch (Exception e) {\n" +
                    "      e.printStackTrace();\n" +
                    "    }\n" +
                    "    return null;");

            TypeSpec typeSpec = TypeSpec.classBuilder("Provider_" + engine.getSimpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(DIProvider.class)
//                  .addSuperinterface(ClassName.get("com.example.basecore","IGet"))
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder("com.ushareit.login.apt", typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
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