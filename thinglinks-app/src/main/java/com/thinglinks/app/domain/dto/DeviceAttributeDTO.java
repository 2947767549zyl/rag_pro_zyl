package com.thinglinks.app.domain.dto;

import lombok.Data;

/**
 * 设备属性信息DTO（独立接口专属）
 * 对应接口：/ent-openapi/api/ent-manager/tenant/v2/device/attribute/get/{deviceId}
 * 接口返回结构：{code, data: {...}, desc, msg}
 */
@Data
public class DeviceAttributeDTO {
    /** 响应码（00000表示成功） */
    private String code;

    /** 设备属性数据体（接口返回的核心数据） */
    private AttributeData data;

    /** 用户端提示信息 */
    private String desc;

    /** 开发端错误排查信息 */
    private String msg;

    /**
     * 设备属性数据体（与接口返回的data字段完全对齐）
     */
    @Data
    public static class AttributeData {
        /** 校准标识（可为null） */
        private String calidNo;

        /** 校准验证码（可为null） */
        private String cvnNo;

        /** ICCID（SIM卡唯一标识，非空） */
        private String iccid;

        /** 车架号（可为null） */
        private String vinNo;
    }
}