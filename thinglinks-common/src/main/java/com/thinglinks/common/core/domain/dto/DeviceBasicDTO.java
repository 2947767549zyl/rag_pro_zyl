package com.thinglinks.common.core.domain.dto;

import lombok.Data;
import java.util.List;

/**
 * 设备详情DTO（匹配途强接口实际返回结构）
 * 对应接口：/ent-openapi/api/ent-manager/tenant/v2/device/detail/get/{deviceId}
 * 接口返回结构：{code, data: {deviceInfo, deviceStatus, deviceAttribute}, desc, msg}
 */
@Data
public class DeviceBasicDTO {
    /** 响应码（00000表示成功） */
    private String code;

    /** 设备详情完整数据体（接口实际返回的三层结构） */
    private BasicData data;

    /** 用户端提示信息 */
    private String desc;

    /** 开发端错误排查信息 */
    private String msg;

    /**
     * 设备详情数据体（与接口返回的data字段完全对齐）
     */
    @Data
    public static class BasicData {
        /** 设备基础信息（必需） */
        private DeviceInfo deviceInfo;

        /** 设备状态信息（必需） */
        private DeviceStatus deviceStatus;

        /** 设备属性信息（必需） */
        private DeviceAttribute deviceAttribute;
    }

    /**
     * 设备基础信息子实体（对应接口的deviceInfo）
     */
    @Data
    public static class DeviceInfo {
        private String activeTime; // 激活时间
        private String createTime; // 创建时间
        private Integer deptId;    // 组织ID
        private String deptName;   // 组织名称
        private String deviceId;   // 设备ID
        private String deviceName; // 设备名称
        private Integer deviceStatus; // 设备状态：0-未激活 1-已激活 -1-已停机
        private Integer disabledStatus; // 禁用状态：0-正常 1-禁用
        private List<String> labels; // 设备标签
        private String lastGateTime; // 最后通信时间
        private String lastPosTime; // 最后定位时间
    }

    /**
     * 设备状态信息子实体（对应接口的deviceStatus）
     */
    @Data
    public static class DeviceStatus {
        private Integer acc; // ACC状态：1-开启 0-关闭
        private Integer charge; // 充电状态：1-充电中 0-未充电
        private String lastGateTime; // 最后通信时间
        private String lastPosTime; // 最后定位时间
        private Integer motionState; // 运动状态：MOTION/STATIC
        private Long motionStateKeepDuration; // 运动状态持续时长（秒）
        private Integer onlineStatus; // 在线状态：ONLINE/OFFLINE
        private Integer powerPercent; // 电量百分比
        private Integer rssi; // 信号量
        private Double voltage; // 电压
        private String workPattern; // 工作模式
        private Long totalMileage; // 总里程（米）
        private Long todayMileage; // 今日里程（米）
        private PosInfo posInfo; // 定位信息

        @Data
        public static class PosInfo {
            private Integer altitude; // 海拔（米）
            private Integer direct; // 方向（0~359度）
            private Double lat; // 纬度
            private Double lng; // 经度
            private Integer posType; // 定位类型：1-卫星 2-WIFI 3-基站
            private Integer satNum; // 卫星数量
            private Float speed; // 速度（m/s）
        }
    }

    /**
     * 设备属性信息子实体（对应接口的deviceAttribute）
     */
    @Data
    public static class DeviceAttribute {
        private String calidNo; // 校准标识
        private String cvnNo;   // 校准验证码
        private String iccid;   // ICCID
        private String vinNo;   // 车架号
    }
}