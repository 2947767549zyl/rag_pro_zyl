package com.thinglinks.app.service;

import com.thinglinks.app.domain.BaiduTrackAnalysis;

import java.util.List;

/**
 * 百度轨迹分析服务接口
 */
public interface BaiduTrackAnalysisService {

    /**
     * 行驶里程分析
     * @param trackPoints 轨迹点列表
     * @param coordType 坐标系类型：bd09ll、gcj02、wgs84
     * @return 分析结果
     */
    BaiduTrackAnalysis.RoadGradeResult analyzeRoadGrade(
            List<BaiduTrackAnalysis.TrackPoint> trackPoints,
            String coordType
    );
}
