package cn.ok.demos.sbaoplog.controller;

import cn.ok.demos.sbaoplog.annotation.ControllerTraceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kyou on 2019-06-24 07:32
 */
@Slf4j
@RestController
public class DemoController {

    @GetMapping("/doDemo")
    @ControllerTraceLog(name = "DemoController.doDemo()")
    public String doDemo() {
        log.info("DemoController.doDemo()...");
        return "OK";
    }
}
