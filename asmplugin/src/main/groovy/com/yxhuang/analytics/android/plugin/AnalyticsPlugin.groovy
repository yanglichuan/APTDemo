import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

// AnalyticsPlugin.groovy
class AnalyticsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // ‘analytics’ 在 build.gradle 中传值使用
        AnalyticsExtension extension = project.extensions.create("analytics", AnalyticsExtension)
        println("------------AnalyticsPlugin apply  extension disableAutoTrack --------------" +  extension.disableAutoTrack)

        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new AnalyticsTransform(extension))
    }
}

/**
 * 定义额外的属性
 */
class AnalyticsExtension {

    /**
     * 是否禁止自动插桩
     */
    boolean disableAutoTrack = false
}
