package com.thinglinks.app.service.impl;

import com.thinglinks.app.domain.dto.DeviceAttributeDTO;
import com.thinglinks.app.domain.dto.DeviceBasicDTO;
import com.thinglinks.app.domain.dto.DeviceStatusDTO;
import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;
import com.thinglinks.app.service.TqiotDeviceService;
import com.thinglinks.app.utils.TqiotApiUtils;
import com.thinglinks.common.config.TqiotConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TqiotDeviceServiceImpl implements TqiotDeviceService {

    @Autowired
    private TqiotApiUtils tqiotApiUtils;

    @Autowired
    private TqiotConfig tqiotConfig;

    @Override
    public DevicePageResponse queryDevicePage(Long pageIndex, Long pageSize, DevicePageRequest.DeviceQueryData queryData) {
        // 分页是 POST 请求，走原有工具类方法
        return tqiotApiUtils.queryDevicePage(pageIndex, pageSize, queryData);
    }

    @Override
    public DevicePageResponse queryDevicePage() {
        return tqiotApiUtils.queryDevicePage();
    }

    @Override
    public DeviceBasicDTO getDeviceDetail(String deviceId) {
        String path = tqiotConfig.getDeviceDetailPath().replace("{deviceId}", deviceId);
        DeviceBasicDTO dto = tqiotApiUtils.doGet(path, DeviceBasicDTO.class);
        checkSuccess(dto);
        return dto;
    }

    @Override
    public DeviceStatusDTO getDeviceStatus(String deviceId) {
        String path = tqiotConfig.getDeviceStatusPath().replace("{deviceId}", deviceId);
        DeviceStatusDTO dto = tqiotApiUtils.doGet(path, DeviceStatusDTO.class);
        checkSuccess(dto);
        return dto;
    }

    @Override
    public DeviceAttributeDTO getDeviceAttribute(String deviceId) {
        String path = tqiotConfig.getDeviceAttributePath().replace("{deviceId}", deviceId);
        DeviceAttributeDTO dto = tqiotApiUtils.doGet(path, DeviceAttributeDTO.class);
        checkSuccess(dto);
        return dto;
    }

    // 统一校验返回码
    private void checkSuccess(Object dto) {
        try {
            String code = BeanUtils.getProperty(dto, "code");
            if (!"00000".equals(code)) {
                throw new RuntimeException("接口调用失败：code=" + code);
            }
        } catch (Exception e) {
            throw new RuntimeException("返回格式异常");
        }
    }
}