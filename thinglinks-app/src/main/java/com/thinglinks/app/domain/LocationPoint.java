package com.thinglinks.app.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 位置轨迹点
 */
@Data
public class LocationPoint {

    /** 设备 ID */
    private String deviceId;

    /** 纬度 */
    private BigDecimal latitude;

    /** 经度 */
    private BigDecimal longitude;

    /** 时间戳（毫秒） */
    private Long timestamp;

    /** 事件类型 */
    private String event;

    /** IMU 传感器数据 */
    private Map<String, Object> imuData;

    /** 后端接收时间 */
    private Date receiveTime;
}
