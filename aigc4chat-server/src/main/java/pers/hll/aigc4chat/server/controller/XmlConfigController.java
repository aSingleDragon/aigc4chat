package pers.hll.aigc4chat.server.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "list", description = "配置文件信息列表")
    public R<List<XmlConfigVO<XmlConfig>>> list() {
        return R.data(xmlConfigService.getXmlConfigBeanMap());
    }

    @GetMapping("/get-by-file-name")
    public R<XmlConfigVO<XmlConfig>> getByFileName(@RequestParam String fileName) {
        return R.data(xmlConfigService.getByFileName(fileName));
    }

    @PostMapping("/save-or-update")
    public R<String> saveOrUpdate(@RequestBody Object xmlConfig, @RequestParam String fileName) {
        xmlConfigService.saveOrUpdate(xmlConfig, fileName);
        return R.success();
    }
}
