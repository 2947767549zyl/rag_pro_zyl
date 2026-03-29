package com.thinglinks.system.mapper;

import com.thinglinks.system.domain.SysGeoFence;

import java.util.List;

public interface SysGeoFenceMapper {
    //
    public SysGeoFence selectSysGeoFenceById(Long id);


    public List<SysGeoFence> selectSysGeoFenceList(SysGeoFence sysGeoFence);

    public int insertSysGeoFence(SysGeoFence sysGeoFence);


    public int updateSysGeoFence(SysGeoFence sysGeoFence);


    public int deleteSysGeoFenceByIds(Long[] ids);
}
