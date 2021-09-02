package cn.tim.annotation_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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

import cn.tim.annotation.DILoginManager;

@AutoService(Processor.class)
public class DILoginProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(DILoginManager.class.getCanonicalName());
    }

    private String createBean(String className) {
        String createStr = String.format("(%s)Class.forName(\"%s\").newInstance()", className, className);
        return createStr;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Login Processor ~~~");
        Set<? extends Element> members = roundEnv.getElementsAnnotatedWith(DILoginManager.class);
        if(members== null || members.size() == 0){
            return true;
        }

        for (Element item : members) {
            /**
             * 所在的类
             */
            TypeElement enclosingElement = (TypeElement) item.getEnclosingElement();

            /**
             * 创建方法 getClassNames 后期asm注入engine的类名用
             */
            MethodSpec.Builder  getClassNamesMethodSpecBuilder = MethodSpec.methodBuilder("getClassNames")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(ParameterizedTypeName.get(
                            ClassName.get("java.util", "TreeSet"),
                            ClassName.get("java.lang", "String")));
            getClassNamesMethodSpecBuilder.addStatement("return null");

            /**
             * 依赖注入，host表示宿主
             */
            MethodSpec.Builder  bindViewMethodSpecBuilder = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(enclosingElement.asType()), "host");

            DILoginManager object = item.getAnnotation(DILoginManager.class);
            if (object == null) {
                continue;
            }

            try {
                bindViewMethodSpecBuilder.addCode("try{\n");
                /**
                 * 创建目标对象
                 */
                createTargetObject(bindViewMethodSpecBuilder, item);
                /**
                 *
                 */
                addEngines(bindViewMethodSpecBuilder);
                bindViewMethodSpecBuilder.addCode("} catch (Exception e) {\n    e.printStackTrace();\n}\n");

                createFile(getClassNamesMethodSpecBuilder,
                        bindViewMethodSpecBuilder,
                        enclosingElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 创建文件
     * @param bindViewMethodSpecBuilder
     * @param enclosingElement
     */
    private void createFile(MethodSpec.Builder getClassNames, MethodSpec.Builder bindViewMethodSpecBuilder, TypeElement enclosingElement) {
        TypeSpec typeSpec = TypeSpec.classBuilder("DILoginIn_" + enclosingElement.getSimpleName())
                .superclass(TypeName.get(enclosingElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(getClassNames.build())
                .addMethod(bindViewMethodSpecBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(getPackageName(enclosingElement), typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加引擎
     *
     * @param bindViewMethodSpecBuilder
     */
    private void addEngines(MethodSpec.Builder bindViewMethodSpecBuilder) {
        bindViewMethodSpecBuilder.addCode("    if(getClassNames()!=null){\n" +
                "            for (String s :  getClassNames()) {\n" +
                "                host.mLoginManager.add((com.example.basecore.IEngine) Class.forName(s).getDeclaredMethod(\"get\").invoke(null));\n" +
                "            }\n" +
                "    }\n");
    }


    /**
     * 创建目标对象
     *
     * @param bindViewMethodSpecBuilder
     * @param item
     */
    private void createTargetObject(MethodSpec.Builder bindViewMethodSpecBuilder, Element item) {
        String className = ClassName.get(item.asType()).toString();
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

//
//public final class DILoginIn_MainActivity extends MainActivity {
//    public static TreeSet<String> getClassNames() {
//        return null;
//    }
//
//    public static void inject(MainActivity host) {
//        try{
//            host.mLoginManager = (cn.tim.apt_demo.LoginManager)Class.forName("cn.tim.apt_demo.LoginManager").newInstance();
//            if(getClassNames()!=null){
//                for (String s :  getClassNames()) {
//                    host.mLoginManager.add((com.example.basecore.IEngine) Class.forName(s).getDeclaredMethod("get").invoke(null));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}


