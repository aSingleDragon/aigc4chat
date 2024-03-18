package pers.hll.aigc4chat.common.base;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.code.XCodeTools;
import pers.hll.aigc4chat.common.base.config.XConfigTools;
import pers.hll.aigc4chat.common.base.config.configs.XConfigs;
import pers.hll.aigc4chat.common.base.http.XHttpTools;
import pers.hll.aigc4chat.common.base.http.executor.XHttpExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 常用的基本的函数的集合和索引
 */
@Slf4j
public final class XTools {
    public static final String CFG_PREFIX = "me.xuxiaoxiao$xtools-common$";

    private XTools() {
    }

    /**
     * 字符串MD5散列
     *
     * @param str 被散列的字符串
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String md5(@Nonnull String str) {
        try {
            return XCodeTools.hash(XCodeTools.HASH_MD5, str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 文件MD5散列
     *
     * @param file 被散列的文件
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String md5(@Nonnull File file) {
        try {
            return XCodeTools.hash(XCodeTools.HASH_MD5, file);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 字符串SHA1散列
     *
     * @param str 被散列的字符串
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String sha1(@Nonnull String str) {
        try {
            return XCodeTools.hash(XCodeTools.HASH_SHA1, str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 文件SHA1散列
     *
     * @param file 被散列的文件
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String sha1(@Nonnull File file) {
        try {
            return XCodeTools.hash(XCodeTools.HASH_SHA1, file);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 使用默认的请求执行器进行HTTP请求，
     * 如需对HTTP请求进行更复杂的配置，请移步XTools.http(XOption option, XRequest request);方法
     *
     * @param request http请求
     * @return 请求的响应体
     */
    @Nonnull
    public static XHttpExecutor.Response http(@Nonnull XHttpExecutor.Request request) {
        return XHttpTools.http(XHttpTools.EXECUTOR, request);
    }

    /**
     * 使用自定义的请求执行器进行HTTP请求
     *
     * @param executor http请求执行器
     * @param request  http请求
     * @return 请求的响应体
     */
    @Nonnull
    public static XHttpExecutor.Response http(@Nonnull XHttpExecutor executor, @Nonnull XHttpExecutor.Request request) {
        return XHttpTools.http(executor, request);
    }

    /**
     * 去除字符串前后的空白符，如果为null则返回null
     *
     * @param str 需要去除前后空白符的字符串
     * @return 去除前后空白符的字符串，如果传入的字符串为null则返回null
     */
    @Nullable
    public static String strTrim(@Nullable String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除字符串数组中每个元素的前后的空白符，如果为null则返回null
     *
     * @param strs 需要去除前后空白符的字符串数组
     * @return 去除每个元素前后空白符的字符串数组，如果传入的字符串数组为null则返回null，字符串中元素为null的返回的数据中也是null
     */
    @Nullable
    public static String[] strTrim(@Nullable String[] strs) {
        if (strs == null) {
            return null;
        } else {
            for (int i = 0, len = strs.length; i < len; i++) {
                strs[i] = (strs[i] == null ? null : strs[i].trim());
            }
            return strs;
        }
    }

    /**
     * 将字符串数组用一个固定的字符串连接起来
     *
     * @param strArr 字符串集合
     * @param glue   用于连接的字符串
     * @return 连接后的字符串
     */
    @Nonnull
    public static String strJoin(@Nonnull String[] strArr, @Nonnull String glue) {
        StringBuilder sbStr = new StringBuilder();
        for (String str : strArr) {
            if (sbStr.length() > 0) {
                sbStr.append(glue);
            }
            sbStr.append(str);
        }
        return sbStr.toString();
    }

    /**
     * 将字符串集合用一个固定的字符串连接起来
     *
     * @param strSet 字符串集合
     * @param glue   用于连接的字符串
     * @return 连接后的字符串
     */
    @Nonnull
    public static String strJoin(@Nonnull Collection<String> strSet, @Nonnull String glue) {
        StringBuilder sbStr = new StringBuilder();
        for (String str : strSet) {
            if (sbStr.length() > 0) {
                sbStr.append(glue);
            }
            sbStr.append(str);
        }
        return sbStr.toString();
    }

    /**
     * 将键值对集合用固定的字符串连接起来
     *
     * @param strMap    键值对集合
     * @param glueInner 连接键和值的字符串
     * @param glueOuter 连接键值对之间的字符串
     * @return 拼接后的字符串
     */
    @Nonnull
    public static String strJoin(@Nonnull Map<?, ?> strMap, @Nonnull String glueInner, @Nonnull String glueOuter) {
        StringBuilder sbStr = new StringBuilder();
        for (Object key : strMap.keySet()) {
            if (sbStr.length() > 0) {
                sbStr.append(glueOuter);
            }
            sbStr.append(key).append(glueInner).append(strMap.get(key));
        }
        return sbStr.toString();
    }

    /**
     * 将字符串保存成文件
     *
     * @param str     要保存成文件的字符串
     * @param path    保存文件的位置
     * @param charset 字符串的编码格式
     * @return 保存后的文件
     * @throws IOException 在保存时可能会发生IO异常
     */
    @Nonnull
    public static File strToFile(@Nullable String str, @Nonnull String path, @Nonnull String charset) throws IOException {
        File file = new File(path);
        try (BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outStream.write(str == null ? new byte[0] : str.getBytes(charset));
            outStream.flush();
        }
        return file;
    }

    /**
     * 将文件读取成字符串
     *
     * @param file    要读取成字符串的文件
     * @param charset 字符串的编码格式
     * @return 读取出的字符串
     * @throws IOException 在读取时可能会发生IO异常
     */
    @Nonnull
    public static String fileToStr(@Nonnull File file, @Nonnull String charset) throws IOException {
        try (FileInputStream fInStream = new FileInputStream(file)) {
            return streamToStr(fInStream, charset);
        }
    }

    /**
     * 将文件复制到另外一个文件
     *
     * @param file 源文件
     * @param path 目标文件的地址
     * @return 目标文件
     * @throws IOException 在复制时可能会发生IO异常
     */
    @Nonnull
    public static File fileToFile(@Nonnull File file, @Nonnull String path) throws IOException {
        File fileTo = new File(path);
        try (FileInputStream fInStream = new FileInputStream(file); FileOutputStream fOutStream = new FileOutputStream(fileTo)) {
            streamToStream(fInStream, fOutStream);
            return fileTo;
        }
    }

    /**
     * 将输入流中的全部数据读取成字符串
     *
     * @param inStream 要读取的输入流，不会关闭该输入流
     * @param charset  字符串的编码格式
     * @return 读取出的字符串
     * @throws IOException 在读取时可能会发生IO异常
     */
    @Nonnull
    public static String streamToStr(@Nonnull InputStream inStream, @Nonnull String charset) throws IOException {
        int count;
        char[] buffer = new char[1024];
        StringBuilder sbStr = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, charset));
        while ((count = bufReader.read(buffer)) > 0) {
            sbStr.append(buffer, 0, count);
        }
        return sbStr.toString();
    }

    /**
     * 将输入流中的全部数据读取成文件
     *
     * @param inStream 要读取的输入流，不会关闭该输入流
     * @param path     要保存的文件的位置
     * @return 保存后的文件
     * @throws IOException 在读取时可能会发生IO异常
     */
    @Nonnull
    public static File streamToFile(@Nonnull InputStream inStream, @Nonnull String path) throws IOException {
        int count;
        byte[] buffer = new byte[1024];
        File file = new File(path);
        BufferedInputStream bufInStream = new BufferedInputStream(inStream);
        try (BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file))) {
            while ((count = bufInStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, count);
            }
            outStream.flush();
        }
        return file;
    }

    /**
     * 将输入流中的全部数据读取到输出流
     *
     * @param inStream  要读取的输入流，不会关闭该输入流
     * @param outStream 要写入的输出流，不会关闭该输出流
     * @throws IOException 输入输出时可能会发生IO异常
     */
    public static void streamToStream(@Nonnull InputStream inStream, @Nonnull OutputStream outStream) throws IOException {
        int count;
        byte[] buffer = new byte[1024];
        BufferedInputStream bufInStream = new BufferedInputStream(inStream);
        BufferedOutputStream bufOutStream = new BufferedOutputStream(outStream);
        while ((count = bufInStream.read(buffer)) > 0) {
            bufOutStream.write(buffer, 0, count);
        }
        bufOutStream.flush();
    }

    /**
     * 判断是否是windows系统
     *
     * @return 是否是windows系统
     */
    public static boolean sysWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 判断是否是MacOS系统
     *
     * @return 是否是MacOS系统
     */
    public static boolean sysMacOS() {
        String osName = System.getProperties().getProperty("os.name").toLowerCase();
        return osName.contains("mac") && !osName.contains("x");
    }

    /**
     * 判断是否是MacOSX系统
     *
     * @return 是否是MacOSX系统
     */
    public static boolean sysMacOSX() {
        String osName = System.getProperties().getProperty("os.name").toLowerCase();
        return osName.contains("mac") && osName.contains("x");
    }

    /**
     * 判断是否是Linux系统
     *
     * @return 是否是Linux系统
     */
    public static boolean sysLinux() {
        String osName = System.getProperties().getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }

    /**
     * 设置配置信息
     *
     * @param key 配置键
     * @param val 配置值
     */
    public static void cfgSet(@Nonnull String key, @Nonnull String val) {
        XConfigTools.X_CONFIGS.cfgSet(key, val);
    }

    /**
     * 获取配置信息值
     *
     * @param key 配置信息键名
     * @return 配置信息值
     */
    @Nullable
    public static String cfgGet(@Nonnull String key) {
        return XConfigTools.X_CONFIGS.cfgGet(key);
    }

    /**
     * 获取或设置配置信息
     *
     * @param key 配置键
     * @param def 配置值为null时设置的默认值
     * @return 当配置值为null时，将def设置为配置值并返回，否则返回原有的配置值并且不做任何更改
     */
    @Nonnull
    public static String cfgDef(@Nonnull String key, @Nonnull String def) {
        return XConfigTools.X_CONFIGS.cfgDef(key, def);
    }

    /**
     * 监听配置信息
     *
     * @param prefix  配置键前缀
     * @param watcher 配置信息监听器
     */
    public static void cfgWatch(@Nonnull String prefix, @Nonnull XConfigs.Watcher watcher) {
        XConfigTools.X_CONFIGS.watcherAdd(prefix, watcher);
    }
}
