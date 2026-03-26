package com.thinglinks.app.controller;

import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;
import com.thinglinks.common.annotation.Anonymous;
import com.thinglinks.app.domain.dto.DeviceAttributeDTO;
import com.thinglinks.app.domain.dto.DeviceBasicDTO;
import com.thinglinks.app.domain.dto.DeviceStatusDTO;
import com.thinglinks.app.service.TqiotDeviceService;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 途强智能平台接口Controller
 */
@RestController
@RequestMapping("/tqiot")
@Anonymous
public class TqiotTestController extends BaseController {

    @Autowired
    private TqiotDeviceService tqiotDeviceService;

    /**
     * 设备分页查询（自定义条件）
     */
    @PostMapping("/device/page")
    public AjaxResult queryDevicePage(@RequestBody DevicePageRequest request) {
        try {
            DevicePageResponse response = tqiotDeviceService.queryDevicePage(
                    request.getPageIndex(),
                    request.getPageSize(),
                    request.getData()
            );
            return AjaxResult.success("设备查询成功", response);
        } catch (Exception e) {
            return AjaxResult.error("设备查询失败：" + e.getMessage());
        }
    }

    /**
     * 设备分页查询（默认条件：第1页，每页10条）
     */
    @PostMapping("/device/page/default")
    public AjaxResult queryDevicePageDefault() {
        try {
            DevicePageResponse response = tqiotDeviceService.queryDevicePage();
            return AjaxResult.success("设备查询成功", response);
        } catch (Exception e) {
            return AjaxResult.error("设备查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备详情
     */
    @GetMapping("/device/detail/{deviceId}")
    public AjaxResult getDeviceDetail(@PathVariable String deviceId) {
        try {
            DeviceBasicDTO dto = tqiotDeviceService.getDeviceDetail(deviceId);
            return AjaxResult.success("设备详情查询成功", dto.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备详情查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备状态
     */
    @GetMapping("/device/status/{deviceId}")
    public AjaxResult getDeviceStatus(@PathVariable String deviceId) {
        try {
            DeviceStatusDTO dto = tqiotDeviceService.getDeviceStatus(deviceId);
            return AjaxResult.success("设备状态查询成功", dto.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备状态查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备属性
     */
    @GetMapping("/device/attribute/{deviceId}")
    public AjaxResult getDeviceAttribute(@PathVariable String deviceId) {
        try {
            DeviceAttributeDTO dto = tqiotDeviceService.getDeviceAttribute(deviceId);
            return AjaxResult.success("设备属性查询成功", dto.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备属性查询失败：" + e.getMessage());
        }
    }
}