package com.thinglinks.system.domain;

import com.thinglinks.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysGeoFence extends BaseEntity {
    //666
    private Long id;
    private String fenceName;
    private String fenceType;    // 1圆形 2多边形 3矩形
    private String coordinates;  // 存储JSON字符串
    private String areaCode;     // 行政区划代码
    private Long chargeRuleId;   // 收费规则ID
    private String status;       // 0正常 1停用
}
