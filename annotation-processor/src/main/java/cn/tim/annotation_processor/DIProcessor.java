package cn.tim.annotation_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import cn.tim.annotation.DIObject;
import cn.tim.annotation.DIProvider;

@AutoService(Processor.class)
public class DIProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> annotations = new TreeSet<>();
//        annotations.add(DIProvider.class.getCanonicalName());
//        annotations.add(DIObject.class.getCanonicalName());
//        return annotations;
        // 规定需要处理的注解
        return Collections.singleton(DIObject.class.getCanonicalName());
    }

    private String createBean(String s) {
        String ddd = String.format("(%s)Class.forName(\"%s\").newInstance()", s, s);
        return ddd;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Login Processor ~~~");
        Set<? extends Element> members = roundEnv.getElementsAnnotatedWith(DIObject.class);
        for (Element item : members) {

            MethodSpec.Builder bindViewMethodSpecBuilder = null;
            TypeElement typeElement = (TypeElement) item.getEnclosingElement();
            if (members.size() > 0) {
                bindViewMethodSpecBuilder = MethodSpec.methodBuilder("inject")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get("android.content", "Context"), "context")
                        .addParameter(ClassName.get(typeElement.asType()), "host");
            } else {
                return true;
            }

            DIObject object = item.getAnnotation(DIObject.class);
            if (object == null) {
                continue;
            }
            try {
                createTargetObject(bindViewMethodSpecBuilder, item);
                addEngines(bindViewMethodSpecBuilder, "context");

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("xxxxxxx22222");


            createFile(bindViewMethodSpecBuilder,typeElement);
        }

        return true;
    }

    /**
     *  创建文件
     * @param bindViewMethodSpecBuilder
     * @param typeElement
     */
    private void createFile(MethodSpec.Builder bindViewMethodSpecBuilder, TypeElement typeElement) {
        TypeSpec typeSpec = TypeSpec.classBuilder("DiLoginIn" + typeElement.getSimpleName())
                .superclass(TypeName.get(typeElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(bindViewMethodSpecBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加引擎
     * @param bindViewMethodSpecBuilder
     */
    private void addEngines(MethodSpec.Builder bindViewMethodSpecBuilder,String param) {
        bindViewMethodSpecBuilder.addCode("    java.util.Set<String> fileNameByPackageName = com.example.basecore.util.ClassUtils.getFileNameByPackageName("+param+", \"com.ushareit.login.apt\");\n\n" +
                "    for (String s : fileNameByPackageName) {\n" +
                "       host.mLoginManager.add((com.example.basecore.IEngine) Class.forName(s).getDeclaredMethod(\"get\").invoke(null));\n" +
                "    }\n");

        bindViewMethodSpecBuilder.addCode("} catch (Exception e) {\n    e.printStackTrace();\n}\n");
    }


    /**
     * 创建目标对象
     * @param bindViewMethodSpecBuilder
     * @param item
     */
    private void createTargetObject(MethodSpec.Builder bindViewMethodSpecBuilder, Element item) {
        String className = ClassName.get(item.asType()).toString();
        bindViewMethodSpecBuilder.addCode("try{\n");
        bindViewMethodSpecBuilder.addCode(String.format(
                "    host.%s = (%s)Class.forName(\"%s\").newInstance();\n",
                item.getSimpleName(),
                className,
                className));
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