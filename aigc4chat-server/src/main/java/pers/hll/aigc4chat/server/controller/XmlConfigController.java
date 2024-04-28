package pers.hll.aigc4chat.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pers.hll.aigc4chat.base.xml.XmlConfig;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.bean.vo.XmlConfigVO;
import pers.hll.aigc4chat.server.service.IXmlConfigService;

import java.util.List;

/**
 * Xml配置控制器
 *
 * @author hll
 * @since 2024/03/21
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Xml配置控制器")
@RequestMapping("/xml-config")
public class XmlConfigController {

    private final IXmlConfigService xmlConfigService;

    @GetMapping("/list")
    @Operation(summary = "Xml配置详情列表", description = "所有的Xml配置详情(如果文件不存在或未配置值, 则值为null)")
    public R<List<XmlConfigVO<XmlConfig>>> list() {
        return R.data(xmlConfigService.getXmlConfigBeanMap());
    }

    @GetMapping("/get-by-file-name")
    @Operation(summary = "Xml配置详情", description = "文件名对应的Xml配置详情(如果文件不存在或未配置值, 则值为null)")
    public R<XmlConfigVO<XmlConfig>> getByFileName(@RequestParam @Parameter(description = "文件名") String fileName) {
        return R.data(xmlConfigService.getByFileName(fileName));
    }

    @PostMapping("/edit")
    @Operation(summary = "编辑Xml配置内容", description = "只能对<Xml配置详情列表>接口里的Xml配置进行编辑")
    public R<String> edit(@RequestBody Object xmlConfig,
                          @RequestParam @Parameter(description = "文件名") String fileName) {
        xmlConfigService.saveOrUpdate(xmlConfig, fileName);
        return R.success();
    }
}
