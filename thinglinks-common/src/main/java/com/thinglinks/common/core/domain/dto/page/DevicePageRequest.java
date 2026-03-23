package com.thinglinks.common.core.domain.dto.page;

import lombok.Data;
import java.util.List;

/**
 * 设备分页查询请求参数
 */
@Data // 替代所有Getter/Setter、equals、hashCode、toString
public class DevicePageRequest {
    /** 页码（从1开始） */
    private Long pageIndex;

    /** 每页条数（最大200） */
    private Long pageSize;

    /** 查询条件 */
    private DeviceQueryData data;

    /**
     * 查询条件子实体
     */
    @Data
    public static class DeviceQueryData {
        /** 组织ID（可为null） */
        private Integer deptId; // 优化：从Object改为Integer，符合实际业务类型

        /** 是否包含下级组织（默认false） */
        private Boolean deptSubordinate = false;

        /** 设备ID集合（精准查询指定设备） */
        private List<String> deviceIds;

        /** 设备标签集合（按标签筛选） */
        private List<String> labels;

        /** 在线状态：1-在线 0-离线（null表示不筛选） */
        private Integer onlineStatus;
    }
}