package com.thinglinks.app.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 百度轨迹分析相关实体
 */
public class BaiduTrackAnalysis {

    /**
     * 轨迹点 DTO
     */
    @Data
    public static class TrackPoint {
        /** 纬度 */
        private BigDecimal latitude;

        /** 经度 */
        private BigDecimal longitude;

        /** 时间戳（秒） */
        private Long timestamp;

        /** 速度（公里/小时） */
        private Double speed;

        /** 方向（0-359 度） */
        private Integer direction;

        /** 定位精度（米） */
        private Integer radius;
    }

    /**
     * 道路等级分析结果
     */
    @Data
    public static class RoadGradeResult {
        /** 状态码 */
        private Integer status;

        /** 响应信息 */
        private String message;

        /** 轨迹点总数 */
        private Integer total;

        /** 总里程（米） */
        private Double distance;

        /** 道路等级信息列表 */
        private List<RoadGradeInfo> roadGradeInfoList;
    }

    /**
     * 道路等级信息
     */
    @Data
    public static class RoadGradeInfo {
        /** 道路等级 */
        private String roadGrade;

        /** 该等级道路的里程（米） */
        private Double distance;
    }
}
