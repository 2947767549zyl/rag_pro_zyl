package com.thinglinks.web.controller.system;

import com.thinglinks.common.annotation.Log;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import com.thinglinks.common.core.page.TableDataInfo;
import com.thinglinks.common.enums.BusinessType;
import com.thinglinks.system.domain.SysGeoFence;
import com.thinglinks.system.service.ISysGeoFenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.thinglinks.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/system/geofence")
public class SysGeoFenceController extends BaseController {

    @Autowired
    private ISysGeoFenceService sysGeoFenceService;

    /**
     * 查询电子围栏列表
     */
    @PreAuthorize("@ss.hasPermi('system:geofence:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGeoFence sysGeoFence) {
        startPage();
        List<SysGeoFence> list = sysGeoFenceService.selectSysGeoFenceList(sysGeoFence);
        System.out.println("666");
        return getDataTable(list);
    }

    /**
     * 获取电子围栏详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:geofence:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysGeoFenceService.selectSysGeoFenceById(id));
    }

    /**
     * 新增电子围栏
     */
    @PreAuthorize("@ss.hasPermi('system:geofence:add')")
    @Log(title = "电子围栏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGeoFence sysGeoFence) {
        sysGeoFence.setCreateBy(getUserId().toString());
        return toAjax(sysGeoFenceService.insertSysGeoFence(sysGeoFence));
    }

    /**
     * 修改电子围栏
     */
    @PreAuthorize("@ss.hasPermi('system:geofence:edit')")
    @Log(title = "电子围栏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGeoFence sysGeoFence) {
        sysGeoFence.setUpdateBy(getUserId().toString());
        return toAjax(sysGeoFenceService.updateSysGeoFence(sysGeoFence));
    }

    /**
     * 删除电子围栏
     */
    @PreAuthorize("@ss.hasPermi('system:geofence:remove')")
    @Log(title = "电子围栏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysGeoFenceService.deleteSysGeoFenceByIds(ids));
    }


}

