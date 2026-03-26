package com.thinglinks.app.domain.dto;

import lombok.Data;

/**
 * 设备状态信息DTO（独立接口专属）
 * 对应接口：/ent-openapi/api/ent-manager/tenant/v2/device/status/get/{deviceId}
 * 接口返回结构：{code, data: {...}, desc, msg}
 */
@Data
public class DeviceStatusDTO {
    /** 响应码（00000表示成功） */
    private String code;

    /** 设备状态数据体（接口返回的核心数据） */
    private StatusData data;

    /** 用户端提示信息 */
    private String desc;

    /** 开发端错误排查信息 */
    private String msg;

    /**
     * 设备状态数据体（与接口返回的data字段完全对齐）
     */
    @Data
    public static class StatusData {
        /** ACC状态：1-开启 0-关闭 */
        private Integer acc;

        /** 充电状态：1-充电中 0-未充电 */
        private Integer charge;

        /** 最后通信时间（ISO格式：yyyy-MM-dd HH:mm:ss） */
        private String lastGateTime;

        /** 最后定位时间（ISO格式：yyyy-MM-dd HH:mm:ss） */
        private String lastPosTime;

        /** 运动状态：1-运动 2-静止（严格Integer类型） */
        private Integer motionState;

        /** 运动状态持续时长（单位：秒） */
        private Long motionStateKeepDuration;

        /** 在线状态：1-在线 0-离线（严格Integer类型） */
        private Integer onlineStatus;

        /** 电量百分比（0-100） */
        private Integer powerPercent;

        /** 信号强度（数值越大信号越好，范围：0-100） */
        private Integer rssi;

        /** 设备电压（单位：V，保留1位小数） */
        private Double voltage;

        /** 工作模式：1-追踪模式 2-普通模式 3-省电模式 */
        private String workPattern;

        /** 总里程（单位：米） */
        private Long totalMileage;

        /** 今日里程（单位：米） */
        private Long todayMileage;

        /** 定位信息（非空） */
        private PosInfo posInfo;
    }

    /**
     * 定位信息子DTO（与接口返回结构完全一致）
     */
    @Data
    public static class PosInfo {
        /** 海拔（单位：米） */
        private Integer altitude;

        /** 方向（0~359度，0为正北） */
        private Integer direct;

        /** 纬度（保留6位小数） */
        private Double lat;

        /** 经度（保留6位小数） */
        private Double lng;

        /** 定位类型：1-卫星 2-WIFI 3-基站 */
        private Integer posType;

        /** 卫星数量（仅卫星定位时有值，否则为0） */
        private Integer satNum;

        /** 速度（单位：米/秒，保留1位小数） */
        private Float speed;
    }
}