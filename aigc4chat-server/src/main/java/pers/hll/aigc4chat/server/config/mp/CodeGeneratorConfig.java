package pers.hll.aigc4chat.server.config.mp;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.apache.ibatis.jdbc.ScriptRunner;
import pers.hll.aigc4chat.server.entity.BaseEntity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

public class CodeGeneratorConfig {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(
            "jdbc:sqlite:/Users/hll/IdeaProjects/aigc/aigc4chat/file/project/db/aigc4chat-sqlite.db",
            "",
            "");


    private static final String PACKAGE_PATH = "pers.hll.aigc4chat.server";

    private static final String MODULE_PATH = "/aigc4chat-server";

    private static final String JAVA = "/src/main/java";

    private static final String RESOURCES = "/src/main/resources";

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String OUTPUT_DIR = USER_DIR + MODULE_PATH + JAVA;

    private static final String XML_PATH = USER_DIR + MODULE_PATH + RESOURCES + "/mapper";

    private static final String INIT_SQL_PATH = USER_DIR + MODULE_PATH + RESOURCES + "/sql/init.sql";

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("hll")
                            .enableSpringdoc()
                            .outputDir(OUTPUT_DIR);
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_PATH)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, XML_PATH));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("wechat_message_handler_config");
                    builder.entityBuilder()
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns("created_time", "updated_time")
                            .enableLombok()
                            //.enableFileOverride()
                            .build();
                    builder.controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            //.enableFileOverride()
                            .build();
                    builder.mapperBuilder()
                            //.enableFileOverride()
                            .build();
                    builder.serviceBuilder()
                            //.enableFileOverride()
                            .build();
                })
                .execute();
    }

    /**
     * 执行数据库初始化脚本
     *
     * @param dataSourceConfig 数据源配置
     */
    protected static void initDataSource(DataSourceConfig dataSourceConfig) throws SQLException, FileNotFoundException {
        Connection conn = dataSourceConfig.getConn();
        InputStream inputStream = new FileInputStream(INIT_SQL_PATH);
        ScriptRunner scriptRunner = new ScriptRunner(conn);
        scriptRunner.setAutoCommit(true);
        assert inputStream != null;
        scriptRunner.runScript(new InputStreamReader(inputStream));
        conn.close();
    }
}
