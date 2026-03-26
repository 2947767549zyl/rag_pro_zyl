package com.thinglinks.app.domain.dto.page;

import lombok.Data;
import java.util.List;

/**
 * 设备分页查询响应
 */
@Data
public class DevicePageResponse {
    /** 响应码（00000表示成功） */
    private String code;

    /** 分页数据 */
    private DevicePageData data;

    /** 用户端提示信息 */
    private String desc;

    /** 开发端错误排查信息 */
    private String msg;

    /**
     * 分页数据子实体
     */
    @Data
    public static class DevicePageData {
        /** 当前页码 */
        private Long pageIndex;

        /** 每页条数 */
        private Long pageSize;

        /** 总页数 */
        private Long pages;

        /** 总记录数 */
        private Long total;

        /** 设备列表数据 */
        private List<DeviceInfo> records;
    }

    /**
     * 设备信息实体（分页列表项）
     */
    @Data
    public static class DeviceInfo {
        /** 设备激活时间 */
        private String activeTime;

        /** 充电状态：0-未充电 1-充电中 */
        private Integer charge;

        /** 设备创建时间 */
        private String createTime;

        /** 所属组织ID */
        private Integer deptId;

        /** 所属组织名称 */
        private String deptName;

        /** 设备唯一ID（核心标识） */
        private String deviceId;

        /** 设备名称 */
        private String deviceName;

        /** 设备状态：0-未激活 1-已激活 -1-已停机 */
        private Integer deviceStatus;

        /** 禁用状态：0-正常 1-禁用 */
        private Integer disabledStatus;

        /** 设备标签集合 */
        private List<String> labels;

        /** 最后通信时间（ISO格式） */
        private String lastGateTime;

        /** 最后定位时间（ISO格式） */
        private String lastPosTime;

        /** 运动状态：1-运动 2-静止 */
        private Integer motionState;

        /** 运动状态开始时间 */
        private String motionStateStartTime;

        /** 在线状态：0-离线 1-在线 */
        private Integer onlineStatus;

        /** 定位信息 */
        private PosInfo posInfo;

        /** 电量百分比 */
        private Integer powerPercent;

        /** 信号强度（数值越大信号越好） */
        private Integer rssi;

        /** 工作模式（1-追踪 2-普通 3-省电） */
        private Integer workPattern;

        /** 工作模式名称（前端展示用） */
        private String workPatternName;
    }

    /**
     * 定位信息实体
     */
    @Data
    public static class PosInfo {
        /** 海拔（单位：米） */
        private Integer altitude;

        /** 方向（0~359度） */
        private Integer direct;

        /** 纬度 */
        private Double lat;

        /** 经度 */
        private Double lng;

        /** 定位类型：1-卫星 2-WIFI 3-基站 */
        private Integer posType;

        /** 卫星数量（仅卫星定位时有值） */
        private Integer satNum;

        /** 速度（单位：米/秒） */
        private Float speed;
    }
}