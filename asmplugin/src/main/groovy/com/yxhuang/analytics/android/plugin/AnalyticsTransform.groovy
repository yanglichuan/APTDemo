import com.android.build.api.transform.*

class AnalyticsTransform extends Transform {

    @Override
    String getName() {
        return "AnalyticsAutoTrack"
    }

    /**
     * 需要处理的数据
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     *  Transform 要操作的内容范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        customTransform(transformInvocation.getContext(), transformInvocation.getInputs(),
                transformInvocation.getOutputProvider(), transformInvocation.incremental)
    }

    void customTransform(Context context, Collection<TransformInput> inputs, TransformOutputProvider outputProvider,
                         boolean isIncremental) {

        if (!isIncremental) {
            outputProvider.deleteAll()
        }
        // 遍历
        inputs.forEach { TransformInput input ->

            // 遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInputs(directoryInput)
            }

            // 遍历 jar
            input.jarInputs.each { JarInput jarInput ->
                handleJarInputs(jarInput)
            }
        }
    }
}