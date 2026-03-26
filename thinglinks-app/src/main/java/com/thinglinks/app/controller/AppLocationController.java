package com.thinglinks.app.controller;

import com.thinglinks.app.domain.BaiduTrackAnalysis;
import com.thinglinks.app.domain.LocationPoint;
import com.thinglinks.app.service.BaiduTrackAnalysisService;
import com.thinglinks.common.annotation.Anonymous;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * App 位置轨迹接口
 * 接收前端上报的经纬度数据
 */
@Slf4j
@Anonymous
@RestController
@RequestMapping("/api/location")
public class AppLocationController extends BaseController {

    @Autowired
    private BaiduTrackAnalysisService baiduTrackAnalysisService;

    /**
     * 存储每个设备的轨迹点列表
     * key: deviceId
     * value: 轨迹点列表
     */
    private static final Map<String, List<LocationPoint>> DEVICE_TRACK_MAP = new ConcurrentHashMap<>();

    /**
     * 接收定期上报的位置数据
     * POST /api/location/periodic-report
     */
    @PostMapping("/periodic-report")
    public AjaxResult periodicReport(@RequestBody Map<String, Object> params) {
        try {
            log.info("收到定期上报数据：{}", params);

            // 1. 解析数据
            String deviceId = (String) params.get("deviceId");
            BigDecimal lat = new BigDecimal(params.get("lat").toString());
            BigDecimal lng = new BigDecimal(params.get("lng").toString());
            Long timestamp = Long.valueOf(params.get("timestamp").toString());
            String event = (String) params.get("event");
            Map<String, Object> imuData = (Map<String, Object>) params.get("imu");

            // 2. 构建轨迹点
            LocationPoint point = new LocationPoint();
            point.setDeviceId(deviceId);
            point.setLatitude(lat);
            point.setLongitude(lng);
            point.setTimestamp(timestamp);
            point.setEvent(event);
            point.setImuData(imuData);
            point.setReceiveTime(new Date());

            // 3. 添加到轨迹列表
            DEVICE_TRACK_MAP.computeIfAbsent(deviceId, k -> new ArrayList<>()).add(point);

            log.info("设备 {} 上报成功，当前轨迹点数：{}", deviceId,
                    DEVICE_TRACK_MAP.get(deviceId).size());

            // 4. 返回成功
            Map<String, Object> result = new HashMap<>();
            result.put("deviceId", deviceId);
            result.put("pointCount", DEVICE_TRACK_MAP.get(deviceId).size());
            result.put("message", "上报成功");

            return success(result);

        } catch (Exception e) {
            log.error("接收位置上报失败", e);
            return error("上报失败：" + e.getMessage());
        }
    }

    /**
     * 获取某设备的轨迹列表
     * GET /api/location/track?deviceId=device_001
     */
    @GetMapping("/track")
    public AjaxResult getTrack(@RequestParam String deviceId) {
        try {
            List<LocationPoint> trackList = DEVICE_TRACK_MAP.getOrDefault(deviceId, new ArrayList<>());

            Map<String, Object> result = new HashMap<>();
            result.put("deviceId", deviceId);
            result.put("count", trackList.size());
            result.put("trackList", trackList);

            return success(result);
        } catch (Exception e) {
            log.error("获取轨迹失败", e);
            return error("获取轨迹失败：" + e.getMessage());
        }
    }

    /**
     * 清除某设备的轨迹数据
     * DELETE /api/location/track?deviceId=device_001
     */
    @DeleteMapping("/track")
    public AjaxResult clearTrack(@RequestParam String deviceId) {
        try {
            DEVICE_TRACK_MAP.remove(deviceId);
            return success("轨迹数据已清除");
        } catch (Exception e) {
            log.error("清除轨迹失败", e);
            return error("清除轨迹失败：" + e.getMessage());
        }
    }

    /**
     * 调用百度 API 分析行驶里程
     * POST /api/location/analyze-mileage?deviceId=device_001&coord_type=wgs84
     */
    @PostMapping("/analyze-mileage")
    public AjaxResult analyzeMileage(
            @RequestParam String deviceId,
            @RequestParam(defaultValue = "wgs84") String coord_type
    ) {
        try {
            List<LocationPoint> trackList = DEVICE_TRACK_MAP.getOrDefault(deviceId, new ArrayList<>());

            if (trackList.isEmpty()) {
                return error("设备轨迹数据为空");
            }

            if (trackList.size() > 2000) {
                return error("轨迹点数量超过限制（最多 2000 个点）");
            }

            log.info("开始分析设备 {} 的行驶里程，共 {} 个轨迹点", deviceId, trackList.size());

            // 1. 转换为百度 API 需要的轨迹点格式
            List<BaiduTrackAnalysis.TrackPoint> baiduPoints = trackList.stream()
                    .map(point -> {
                        BaiduTrackAnalysis.TrackPoint baiduPoint = new BaiduTrackAnalysis.TrackPoint();
                        baiduPoint.setLatitude(point.getLatitude());
                        baiduPoint.setLongitude(point.getLongitude());
                        // 百度 API 需要秒级时间戳
                        baiduPoint.setTimestamp(point.getTimestamp() / 1000);
                        return baiduPoint;
                    })
                    .collect(Collectors.toList());

            // 2. 调用百度 API 进行分析
            BaiduTrackAnalysis.RoadGradeResult result =
                    baiduTrackAnalysisService.analyzeRoadGrade(baiduPoints, coord_type);

            // 3. 检查返回状态
            if (result.getStatus() != 0) {
                return error("百度 API 调用失败：" + result.getMessage());
            }

            // 4. 构建返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("deviceId", deviceId);
            response.put("totalPoints", result.getTotal());
            response.put("totalDistance", result.getDistance());
            response.put("totalDistanceKm", result.getDistance() / 1000.0);
            response.put("roadGradeInfo", result.getRoadGradeInfoList());

            // 5. 格式化道路等级说明
            List<Map<String, Object>> formattedRoadInfo = new ArrayList<>();
            for (BaiduTrackAnalysis.RoadGradeInfo info : result.getRoadGradeInfoList()) {
                Map<String, Object> roadInfo = new HashMap<>();
                roadInfo.put("roadGrade", info.getRoadGrade());
                roadInfo.put("roadGradeName", getRoadGradeName(info.getRoadGrade()));
                roadInfo.put("distance", info.getDistance());
                roadInfo.put("distanceKm", info.getDistance() / 1000.0);
                roadInfo.put("percentage", String.format("%.2f%%",
                        (info.getDistance() / result.getDistance()) * 100));
                formattedRoadInfo.add(roadInfo);
            }
            response.put("roadGradeDetail", formattedRoadInfo);

            log.info("行驶里程分析成功，总里程：{} 米", result.getDistance());

            return success(response);

        } catch (Exception e) {
            log.error("行驶里程分析失败", e);
            return error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取道路等级中文名称
     */
    private String getRoadGradeName(String roadGrade) {
        switch (roadGrade) {
            case "ROAD_GRADE_VILLAGE":
                return "乡镇村道";
            case "ROAD_GRADE_SIDEWALK":
                return "步行道路";
            case "ROAD_GRADE_PROVINCIAL":
                return "省道";
            case "ROAD_GRADE_OTHER":
                return "其它道路";
            case "ROAD_GRADE_NINE":
                return "特服道路";
            case "ROAD_GRADE_NATIONAL":
                return "国道";
            case "ROAD_GRADE_HIGHWAY":
                return "高速道路";
            case "ROAD_GRADE_FERRY":
                return "轮渡";
            case "ROAD_GRADE_COUNTY":
                return "县道";
            case "ROAD_GRADE_CITYHIGHWAY":
                return "城市高速";
            default:
                return roadGrade;
        }
    }
}
