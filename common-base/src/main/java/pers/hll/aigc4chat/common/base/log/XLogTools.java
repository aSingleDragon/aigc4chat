package pers.hll.aigc4chat.common.base.log;

import pers.hll.aigc4chat.common.base.XTools;
import pers.hll.aigc4chat.common.base.config.XConfigTools;
import pers.hll.aigc4chat.common.base.log.logger.XLogger;
import pers.hll.aigc4chat.common.base.log.logger.impl.XLoggerImpl;

/**
 * 日志工具类
 */
public class XLogTools {
    public static final String CFG_LOGGER = XTools.CFG_PREFIX + "log.logger";
    public static final String CFG_LOGGER_DEFAULT = XLoggerImpl.class.getName();

    /**
     * 默认日志记录器
     */
    public static final XLogger LOGGER = XConfigTools.supply(XTools.cfgDef(XLogTools.CFG_LOGGER, XLogTools.CFG_LOGGER_DEFAULT).trim());
}
