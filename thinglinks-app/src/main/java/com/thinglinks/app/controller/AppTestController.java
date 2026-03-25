package com.thinglinks.app.controller;

import com.thinglinks.common.annotation.Anonymous;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App 测试接口
 */
@RestController
@RequestMapping("/app/test")
public class AppTestController extends BaseController {

    @GetMapping("/hello")
    @Anonymous
    public AjaxResult hello() {
        return success("App 模块加载成功！欢迎使用 Thinglinks-App");
    }

    @GetMapping("/info")
    @Anonymous
    public AjaxResult info() {
        return success("当前时间：" + System.currentTimeMillis());
    }
}
