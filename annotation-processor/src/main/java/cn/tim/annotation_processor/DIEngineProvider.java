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

@AutoService(Processor.class)
public class DIEngineProvider extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(DIEngine.class.getCanonicalName());
    }

//    Set<String> allName = new HashSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("add DIEngine");

        Element firstElement = null;
        Set<? extends Element> engines = roundEnv.getElementsAnnotatedWith(cn.tim.annotation.DIEngine.class);
        for (Element engine : engines) {
            String engineName = ClassName.get(engine.asType()).toString();
//            allName.add(engineName);
            firstElement = engine;

            if (firstElement == null) {
                return true;
            }

            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("get")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.get(firstElement.asType()));

            String className = ClassName.get(firstElement.asType()).toString();

            bindViewMethodSpecBuilder.addCode("" +
                    " try {\n" +
                    "      return (" + className + ")Class.forName(\"" + className + "\").newInstance();\n" +
                    "    } catch (Exception e) {\n" +
                    "      e.printStackTrace();\n" +
                    "    }\n" +
                    "    return null;");

            TypeSpec typeSpec = TypeSpec.classBuilder("Provider_" + firstElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addSuperinterface(ClassName.get("com.example.basecore","IGet"))
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder("com.a.b.c", typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//
//
//
//        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DIActivity.class);
//        for (Element element : elements) {
//            // 判断是否Class
//            TypeElement typeElement = (TypeElement) element;
//            List<? extends Element> members = elementUtils.getAllMembers(typeElement);
//
//            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("inject")
//                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                    .returns(TypeName.VOID)
//                    .addParameter(ClassName.get(typeElement.asType()), "activity");
//            for (Element item : members) {
//
//                DIObject object = item.getAnnotation(DIObject.class);
//                if (object == null) {
//                    continue;
//                }
//
//
//                try {
//                    String className = ClassName.get(item.asType()).toString();
//                    bindViewMethodSpecBuilder.addCode(String.format(
//                            "try {\n   activity.%s = (%s)Class.forName(\"%s\").newInstance();\n } catch (Exception e) { \n   e.printStackTrace();\n}\n",
//                            item.getSimpleName(),
//                            className,
//                            className));
//
//
//                    bindViewMethodSpecBuilder.addCode("try{\n");
//
//                    for (String engine : allName) {
//
//                        engine = String.format("(%s)Class.forName(\"%s\").newInstance()",
//                                engine, engine);
//
//                        bindViewMethodSpecBuilder.addStatement(String.format(
//                                "   activity.%s.add(%s)",
//                                item.getSimpleName(),
//                                engine, engine));
//                    }
//
//                    bindViewMethodSpecBuilder.addCode("} catch (Exception e) {\n    e.printStackTrace();\n}\n");
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            TypeSpec typeSpec = TypeSpec.classBuilder("DILogin")
//                    .superclass(TypeName.get(typeElement.asType()))
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .addMethod(bindViewMethodSpecBuilder.build())
//                    .build();
//            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
//            try {
//                javaFile.writeTo(processingEnv.getFiler());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
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