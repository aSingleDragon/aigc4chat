package pers.hll.aigc4chat.base.util.converter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import pers.hll.aigc4chat.base.constant.StringPool;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Converter代码生成工具类<br>
 * 基于 Velocity 模版引擎 生成 Converter<br>
 * 使用此工具类时，需要注意以下几点：
 * <ol>
 *     <li>当不指定生成文件目录时，默认生成在调用此工具类方法的类的所在目录下，该目录是基于Java项目目录规范计算出来的</li>
 *     <li>在使用{@link lombok.Data}注解一个Java Bean时，所有实例变量类型应为基础数据类型的包装类型，
 *     否则需要手动去处理getXxx/isXxx的框架解析问题</li>
 * </ol>
 * @author hll
 * @since 2024/04/26
 */
@Slf4j
@UtilityClass
public class ConverterUtil {

    private final String BEAN_CONVERTER = "BeanConverter.vm";

    private final String SEP = File.separator;

    /**
     * 构造 VelocityContext
     *
     * @param dir         生成的<类>要存放的路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @param withSuperClassFields 是否拷贝父类属性
     * @return VelocityContext
     */
    private VelocityContext createVelocityContext(String dir, Class<?> sourceClass, Class<?> targetClass,
                                                         boolean withSuperClassFields, String converterName) {
        VelocityContext velocityContext = new VelocityContext();

        // 用于生成java文件的类注释 获取git用户名/邮箱有点麻烦 有需要的自己手动改吧
        velocityContext.put("author", System.getProperty("user.name"));
        velocityContext.put("since", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        // srcClassFullName e.g. com.yhsf.common.generator.converter.SrcTest 用于生成java文件中<类>的导入
        String srcClassFullName = sourceClass.getName();
        String targetClassFullName = targetClass.getName();

        velocityContext.put("package", getPackage(dir));

        // srcClassName e.g. SrcTest 用于生成java文件中<类>的命名
        String srcClassName = getSimpleClassName(srcClassFullName);
        String targetClassName = getSimpleClassName(targetClassFullName);

        converterName = StringUtils.isNotBlank(converterName) ? converterName : targetClassName + "Converter";
        velocityContext.put("converterName", converterName);

        // srcClassVarName e.g. srcTest 用于生成java文件中<变量>的命名
        String srcClassVarName = StringUtils.uncapitalize(srcClassName);
        String targetClassVarName = StringUtils.uncapitalize(targetClassName);

        velocityContext.put("srcClassFullName", srcClassFullName);
        velocityContext.put("targetClassFullName", targetClassFullName);
        velocityContext.put("srcClassName", srcClassName);
        velocityContext.put("targetClassName", targetClassName);
        velocityContext.put("srcClassVarName", srcClassVarName);
        velocityContext.put("targetClassVarName", targetClassVarName);

        List<String> list = BeanCopyUtil.copyProperties(sourceClass, targetClass, srcClassVarName, targetClassVarName,
                withSuperClassFields);

        velocityContext.put("copyPropertyList", list);

        return velocityContext;
    }

    /**
     * 非幂等操作(insert/update)的Converter便捷生成
     * @param sourceClass 源类
     * @param targetClass 目标类
     */
    public void writeCommand(Class<?> sourceClass, Class<?> targetClass) {
        // com.yhsf.wms.common.entity.rule.req.directory.RuleDirectoryUpdateCommand
        String srcClassName = sourceClass.getName();
        srcClassName = srcClassName.substring(srcClassName.lastIndexOf(".") + 1);
        ConverterUtil.write(sourceClass, targetClass, true, "From" + srcClassName + "Converter");
    }

    /**
     * 构造 VelocityContext
     *
     * @param dir         生成的<类>要存放的路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return VelocityContext
     */
    private VelocityContext createVelocityContext(String dir, Class<?> sourceClass, Class<?> targetClass) {
        return createVelocityContext(dir, sourceClass, targetClass, false, null);
    }

    /**
     * 在"调用此方法的类"所在的路径下生成Converter的java文件
     * <p>
     * "调用此方法的类"所在的路径 为IDEA中Spring项目的标准路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @param withSuperClassFields 是否拷贝父类属性
     */
    public void write(Class<?> sourceClass, Class<?> targetClass,
                             boolean withSuperClassFields) {
        write(getInvokerLocation(), sourceClass, targetClass, withSuperClassFields, null);
    }

    /**
     * 在"调用此方法的类"所在的路径下生成Converter的java文件
     * <p>
     * "调用此方法的类"所在的路径 为IDEA中Spring项目的标准路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @param withSuperClassFields 是否拷贝父类属性
     * @param converterName 自定义生成的Converter的Java文件名
     */
    public void write(Class<?> sourceClass, Class<?> targetClass,
                             boolean withSuperClassFields, String converterName) {
        write(getInvokerLocation(), sourceClass, targetClass, withSuperClassFields, converterName);
    }

    /**
     * 在"调用此方法的类"所在的路径下生成Converter的java文件
     * 此方法对 "调用此方法的类"所在的路径 为IDEA中Spring项目的标准路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     */
    public void write(Class<?> sourceClass, Class<?> targetClass) {
        write(sourceClass, targetClass, false);
    }

    /**
     * 在 dir 目录下生成Converter的java文件
     *
     * @param dir         生成的<类>要存放的路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @param withSuperClassFields 是否拷贝父类属性
     */
    public void write(String dir, Class<?> sourceClass, Class<?> targetClass,
                             boolean withSuperClassFields, String converterName) {


        VelocityEngine velocityEngine = getInstance();
        Template template = velocityEngine.getTemplate(BEAN_CONVERTER);

        VelocityContext velocityContext =
                createVelocityContext(dir, sourceClass, targetClass, withSuperClassFields, converterName);

        String converterFileName = velocityContext.get("converterName") + ".java";

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        String path = dir + SEP + converterFileName;

        try (OutputStream outputStream =  Files.newOutputStream(new File(path).toPath())) {
            IOUtils.write(
                    stringWriter.toString(),
                    outputStream,
                    StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            log.error("Template file read exception:", ioException);
        } finally {
            try {
                stringWriter.close();
            } catch (IOException ioException) {
                log.error("Template file write exception:", ioException);
            }
        }
    }

    /**
     * 在 dir 目录下生成Converter的java文件
     *
     * @param dir         生成的<类>要存放的路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     */
    public void write(String dir, Class<?> sourceClass, Class<?> targetClass) {
        write(dir, sourceClass, targetClass, false, null);
    }

    /**
     * 在 dir 目录下生成Converter的java文件
     *
     * @param dir         生成的<类>要存放的路径
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @param converterName 自定义生成的Converter的Java文件名
     */
    public void write(String dir, Class<?> sourceClass, Class<?> targetClass,
                             String converterName) {
        write(dir, sourceClass, targetClass, false, converterName);
    }

    /**
     * 获得 VelocityEngine 实例
     *
     * @return VelocityEngine 实例
     */
    private VelocityEngine getInstance() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        return velocityEngine;
    }

    /**
     * 获得简短类名
     *
     * @param fullClassName 完整类名 e.g. com.yhsf.common.generator.converter.SrcTest
     * @return 简短类名 e.g. SrcTest
     */
    private String getSimpleClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(StringPool.DOT) + 1);
    }

    /**
     * 这里约定 dir 为生成的<类>要存放的路径 路径形式 为 .../com/...
     * <p>
     * "/com..." 为包名 且 包名中不能重复出现 com
     * <p>
     * e.g.
     * <blockquote><pre>
     *     合法dir: "./common/src/main/java/com/yhsf/common/generator/converter"
     *     非法dir: "./common/src/main/java/com/yhsf/com/test/test"
     * </pre></blockquote>
     *
     * @param dir 要生成文件存放的目录
     * @return java文件所在包名 包名 com.yhsf.common.generator.converter
     */
    private String getPackage(String dir) {
        String packagePrefix = SEP + "pers" + SEP;
        return dir.substring(dir.lastIndexOf(packagePrefix) + 1).replace(SEP, StringPool.DOT);
    }

    private String getInvokerLocation() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // 调用这个工具类方法的类的类名 e.g. com.yhsf.service.system.test.Main
        String callerClassName = stackTraceElements[stackTraceElements.length - 1].getClassName();
        Class<?> callerClass;
        try {
            callerClass = Class.forName(callerClassName);
        } catch (ClassNotFoundException classNotFoundException) {
            log.error("Class [{}] not found exception:", callerClassName, classNotFoundException);
            return null;
        }
        // 编译后的class文件所在位置 e.g. .../wms-server/service-system/service-system-core/target/classes/
        String callerClassPath = callerClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        StringBuilder stringBuilder = new StringBuilder();
        // /target/classes/
        String targetClasses = SEP + "target" + SEP + "classes" + SEP;
        // /src/main/java/
        String srcMainJava = SEP + "src" + SEP + "main" + SEP + "java" + SEP;
        // com.yhsf.service.system.test.Main => com/yhsf/service/system/test
        String packagePath = callerClassName.replace(StringPool.DOT, SEP);
        packagePath = packagePath.substring(0, packagePath.lastIndexOf(SEP));

        stringBuilder
                .append(callerClassPath, 0, callerClassPath.lastIndexOf(targetClasses))
                .append(srcMainJava)
                .append(packagePath);

        return stringBuilder.toString();
    }
}