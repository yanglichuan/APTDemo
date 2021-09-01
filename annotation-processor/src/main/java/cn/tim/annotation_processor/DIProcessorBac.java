//package cn.tim.annotation_processor;
//
//import com.google.auto.service.AutoService;
//import com.squareup.javapoet.ClassName;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.TypeName;
//import com.squareup.javapoet.TypeSpec;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.Modifier;
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.util.Elements;
//
//import cn.tim.annotation.DIActivity;
//import cn.tim.annotation.DIEngine;
//import cn.tim.annotation.DIObject;
//
//@AutoService(Processor.class)
//@Deprecated
//public class DIProcessorBac extends AbstractProcessor {
//    private Elements elementUtils;
//
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        // 规定需要处理的注解
//        return Collections.singleton(DIActivity.class.getCanonicalName());
//    }
//
//    Set<String> allName = new HashSet<>();
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        System.out.println("DIProcessor");
//
//        Set<? extends Element> engines = roundEnv.getElementsAnnotatedWith(DIEngine.class);
//        for (Element engine : engines) {
//            String engineName = ClassName.get(engine.asType()).toString();
//            allName.add(engineName);
////            System.out.println(engineName);
//        }
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
////
////                try {
////                    String className = ClassName.get(item.asType()).toString();
////                    bindViewMethodSpecBuilder.addStatement(String.format(
////                            " try {activity.%s = (%s)Class.forName(\"%s\").newInstance(); } catch (Exception e) { e.printStackTrace();}"+
////                            " activity.%s.add(%s);",
////                            item.getSimpleName(),
////                            className,
////                            className,
////                            engine1));
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//
////                DIView diView = item.getAnnotation(DIView.class);
////                if (diView == null) {
////                    continue;
////                }
////                bindViewMethodSpecBuilder.addStatement(String.format(
////                        "activity.%s = (%s) activity.findViewById(%s)",
////                        item.getSimpleName(),
////                        ClassName.get(item.asType()).toString(),
////                        diView.value()));
//            }
//            TypeSpec typeSpec = TypeSpec.classBuilder("DI" + element.getSimpleName())
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
//        return true;
//    }
//
//    private String getPackageName(TypeElement type) {
//        return elementUtils.getPackageOf(type).getQualifiedName().toString();
//    }
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        elementUtils = processingEnv.getElementUtils();
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.RELEASE_8;
//    }
//}