package com.thinglinks.system.service;

import com.thinglinks.system.domain.SysGeoFence;

import java.util.List;

public interface ISysGeoFenceService {
    public SysGeoFence selectSysGeoFenceById(Long id);
    public List<SysGeoFence> selectSysGeoFenceList(SysGeoFence sysGeoFence);
    public int insertSysGeoFence(SysGeoFence sysGeoFence);
    public int updateSysGeoFence(SysGeoFence sysGeoFence);
    public int deleteSysGeoFenceByIds(Long[] ids);
    public boolean isPointInFence(Double lng, Double lat, SysGeoFence fence);

}
