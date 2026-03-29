package com.thinglinks.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.thinglinks.common.utils.DateUtils;
import com.thinglinks.system.domain.SysGeoFence;
import com.thinglinks.system.mapper.SysGeoFenceMapper;
import com.thinglinks.system.service.ISysGeoFenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Path2D;
import java.util.Collections;
import java.util.List;

@Service
public class SysGeoFenceServiceImpl implements ISysGeoFenceService {

    @Autowired
    private SysGeoFenceMapper sysGeoFenceMapper;

    @Override
    public SysGeoFence selectSysGeoFenceById(Long id) {
        return sysGeoFenceMapper.selectSysGeoFenceById(id);
    }

    @Override
    public List<SysGeoFence> selectSysGeoFenceList(SysGeoFence sysGeoFence) {
        return sysGeoFenceMapper.selectSysGeoFenceList(sysGeoFence);
    }

    @Override
    public int insertSysGeoFence(SysGeoFence sysGeoFence) {
        sysGeoFence.setCreateTime(DateUtils.getNowDate());
        return sysGeoFenceMapper.insertSysGeoFence(sysGeoFence);
    }

    @Override
    public int updateSysGeoFence(SysGeoFence sysGeoFence) {
        sysGeoFence.setUpdateTime(DateUtils.getNowDate());
        return sysGeoFenceMapper.updateSysGeoFence(sysGeoFence);
    }

    @Override
    public int deleteSysGeoFenceByIds(Long[] ids) {
        return sysGeoFenceMapper.deleteSysGeoFenceByIds(ids);
    }

    @Override
    public boolean isPointInFence(Double lng, Double lat, SysGeoFence fence) {
        if (lng == null || lat == null || fence.getCoordinates() == null) return false;
        String type = fence.getFenceType();
        String coords = fence.getCoordinates();

        try {
            if ("1".equals(type)) { // 圆形
                JSONObject json = JSON.parseObject(coords);
                JSONArray center = json.getJSONArray("center");
                double radius = json.getDouble("radius");
                return getDistance(lng, lat, center.getDouble(0), center.getDouble(1)) <= radius;
            } else { // 多边形(2) 或 矩形(3)
                JSONArray pathArr = JSON.parseArray(coords);
                Path2D.Double path = new Path2D.Double();
                for (int i = 0; i < pathArr.size(); i++) {
                    JSONArray point = pathArr.getJSONArray(i);
                    if (i == 0) path.moveTo(point.getDouble(0), point.getDouble(1));
                    else path.lineTo(point.getDouble(0), point.getDouble(1));
                }
                path.closePath();
                return path.contains(lng, lat);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private double getDistance(double n1, double t1, double n2, double t2) {
        double R = 6371000;
        double dLat = Math.toRadians(t2 - t1);
        double dLon = Math.toRadians(n2 - n1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(t1)) * Math.cos(Math.toRadians(t2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
