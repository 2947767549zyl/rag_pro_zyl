package com.thinglinks.app.controller;

import com.thinglinks.common.annotation.Anonymous;
import com.thinglinks.common.config.TqiotConfig;
import com.thinglinks.app.domain.dto.DeviceAttributeDTO;
import com.thinglinks.app.domain.dto.DeviceBasicDTO;
import com.thinglinks.app.domain.dto.DeviceStatusDTO;
import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;
import com.thinglinks.app.utils.TqiotApiUtils;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 途强智能平台接口Controller
 * 提供Token获取、设备分页、设备详情等接口
 */
@RestController
@RequestMapping("/tqiot")
public class TqiotTestController extends BaseController {

    @Autowired
    private TqiotApiUtils tqiotApiUtils;

    @Autowired
    private TqiotConfig tqiotConfig;

    /**
     * 获取平台认证Token
     */
    @Anonymous
    @PostMapping("/getToken")
    public AjaxResult getToken() {
        try {
            String token = tqiotApiUtils.getAccessToken();
            return AjaxResult.success("Token获取成功", token);
        } catch (Exception e) {
            return AjaxResult.error("Token获取失败：" + e.getMessage());
        }
    }

    /**
     * 设备分页查询（自定义条件）
     */
    @Anonymous
    @PostMapping("/device/page")
    public AjaxResult queryDevicePage(@RequestBody DevicePageRequest request) {
        try {
            DevicePageResponse response = tqiotApiUtils.queryDevicePage(
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
    @Anonymous
    @PostMapping("/device/page/default")
    public AjaxResult queryDevicePageDefault() {
        try {
            DevicePageResponse response = tqiotApiUtils.queryDevicePage();
            return AjaxResult.success("设备查询成功", response);
        } catch (Exception e) {
            return AjaxResult.error("设备查询失败：" + e.getMessage());
        }
    }

    /**
     * 【匿名访问】获取设备详情（自动获取Token）
     */
    @Anonymous
    @GetMapping("/device/detail/{deviceId}")
    public AjaxResult getDeviceDetail(@PathVariable String deviceId) {
        try {
            DeviceBasicDTO deviceDetail = tqiotApiUtils.getDeviceDetail(deviceId);
            return AjaxResult.success("设备详情查询成功", deviceDetail.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备详情查询失败：" + e.getMessage());
        }
    }

    /**
     * @param deviceId 设备ID
     * @return 设备状态信息
     */
    @Anonymous
    @GetMapping("/device/status/{deviceId}")
    public AjaxResult getDeviceStatus(@PathVariable String deviceId) {
        try {
            DeviceStatusDTO deviceStatus = tqiotApiUtils.getDeviceStatus(deviceId);
            return AjaxResult.success("设备状态查询成功", deviceStatus.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备状态查询失败：" + e.getMessage());
        }
    }

    /**
     * @param deviceId 设备ID
     * @return 设备属性（ICCID、车架号、校准标识等）
     */
    @Anonymous
    @GetMapping("/device/attribute/{deviceId}")
    public AjaxResult getDeviceAttribute(@PathVariable String deviceId) {
        try {
            DeviceAttributeDTO deviceAttribute = tqiotApiUtils.getDeviceAttribute(deviceId);
            return AjaxResult.success("设备属性查询成功", deviceAttribute.getData());
        } catch (Exception e) {
            return AjaxResult.error("设备属性查询失败：" + e.getMessage());
        }
    }

}