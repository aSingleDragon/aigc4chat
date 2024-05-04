package pers.hll.aigc4chat.server.config;

/**
 * 线程池名称
 *
 * @author hll
 * @since 2024/05/04
 */
public final class ThreadPoolName {

    private ThreadPoolName() {}

    public static final String LOGIN = "loginTaskScheduler";

    public static final String ASYNC_LOGIN = "asyncLoginTaskScheduler";
}
