package com.thinglinks.app.service;

import com.thinglinks.app.domain.dto.DeviceAttributeDTO;
import com.thinglinks.app.domain.dto.DeviceBasicDTO;
import com.thinglinks.app.domain.dto.DeviceStatusDTO;
import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;

public interface TqiotDeviceService {
    // 分页
    DevicePageResponse queryDevicePage(Long pageIndex, Long pageSize, DevicePageRequest.DeviceQueryData queryData);

    // 简单分页
    DevicePageResponse queryDevicePage();

    // 设备详情
    DeviceBasicDTO getDeviceDetail(String deviceId);

    // 设备状态
    DeviceStatusDTO getDeviceStatus(String deviceId);

    // 设备属性
    DeviceAttributeDTO getDeviceAttribute(String deviceId);

}
