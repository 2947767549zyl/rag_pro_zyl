package com.thinglinks.app.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.thinglinks.app.config.BaiduApiConfig;
import com.thinglinks.app.domain.BaiduTrackAnalysis;
import com.thinglinks.app.service.BaiduTrackAnalysisService;
import com.thinglinks.app.utils.BaiDuHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度轨迹分析服务实现
 */
@Slf4j
@Service
public class BaiduTrackAnalysisServiceImpl implements BaiduTrackAnalysisService {

    @Autowired
    private BaiduApiConfig baiduApiConfig;

    @Override
    public BaiduTrackAnalysis.RoadGradeResult analyzeRoadGrade(
            List<BaiduTrackAnalysis.TrackPoint> trackPoints,
            String coordType
    ) {
        log.info("开始调用百度 API 进行道路等级分析，轨迹点数：{}", trackPoints.size());

        try {
            // 1. 构建轨迹点数组（JSON 字符串）
            JSONArray pointList = new JSONArray();
            for (BaiduTrackAnalysis.TrackPoint point : trackPoints) {
                JSONObject pointJson = new JSONObject();
                pointJson.put("latitude", point.getLatitude().doubleValue());
                pointJson.put("longitude", point.getLongitude().doubleValue());
                pointJson.put("loc_time", point.getTimestamp());
                pointJson.put("coord_type_input", coordType);

                // 可选参数
                if (point.getSpeed() != null) {
                    pointJson.put("speed", point.getSpeed());
                }
                if (point.getDirection() != null) {
                    pointJson.put("direction", point.getDirection());
                }
                if (point.getRadius() != null) {
                    pointJson.put("radius", point.getRadius());
                }

                pointList.add(pointJson);
            }

            String pointListJson = pointList.toJSONString();
            log.info("轨迹点数据：{}", pointListJson);

            // 2. 构建请求参数（x-www-form-urlencoded 格式）
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("ak", baiduApiConfig.getAk());
            requestParams.put("point_list", pointListJson);

            log.info("请求百度 API 参数：ak={}, point_list={}", baiduApiConfig.getAk(), pointListJson);

            // 3. 发送 HTTP POST 请求（x-www-form-urlencoded 格式）
            String responseJson = BaiDuHttpUtils.postForm(
                    BaiduApiConfig.ROAD_GRADE_API_URL,
                    requestParams
            );

            log.info("百度 API 返回结果：{}", responseJson);

            // 4. 解析返回结果
            if (responseJson == null) {
                throw new RuntimeException("百度 API 请求失败，返回为空");
            }

            return parseRoadGradeResult(responseJson);

        } catch (Exception e) {
            log.error("调用百度 API 进行道路等级分析失败", e);
            throw new RuntimeException("道路等级分析失败：" + e.getMessage(), e);
        }
    }

    /**
     * 解析百度 API 返回结果
     */
    private BaiduTrackAnalysis.RoadGradeResult parseRoadGradeResult(String responseJson) {
        JSONObject response = JSON.parseObject(responseJson);

        BaiduTrackAnalysis.RoadGradeResult result = new BaiduTrackAnalysis.RoadGradeResult();
        result.setStatus(response.getInteger("status"));
        result.setMessage(response.getString("message"));
        result.setTotal(response.getInteger("total"));
        result.setDistance(response.getDouble("distance"));

        // 解析道路等级信息
        JSONArray roadGradeInfoArray = response.getJSONArray("road_grade_info");
        if (roadGradeInfoArray != null) {
            List<BaiduTrackAnalysis.RoadGradeInfo> roadGradeInfoList = new ArrayList<>();

            for (int i = 0; i < roadGradeInfoArray.size(); i++) {
                JSONObject roadGradeJson = roadGradeInfoArray.getJSONObject(i);
                BaiduTrackAnalysis.RoadGradeInfo info = new BaiduTrackAnalysis.RoadGradeInfo();
                info.setRoadGrade(roadGradeJson.getString("road_grade"));
                info.setDistance(roadGradeJson.getDouble("distance"));
                roadGradeInfoList.add(info);
            }

            result.setRoadGradeInfoList(roadGradeInfoList);
        }

        return result;
    }
}
