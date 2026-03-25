package com.thinglinks.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thinglinks.common.utils.PageUtils;
import com.thinglinks.common.annotation.Log;
import com.thinglinks.common.core.controller.BaseController;
import com.thinglinks.common.core.domain.AjaxResult;
import com.thinglinks.common.enums.BusinessType;
import com.thinglinks.system.domain.TUbiRate;
import com.thinglinks.system.service.ITUbiRateService;
import com.thinglinks.common.utils.poi.ExcelUtil;
import com.thinglinks.common.core.page.TableDataInfo;

/**
 * UBI基础费率配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/system/rate/ubi")
public class TUbiRateController extends BaseController
{
    @Autowired
    private ITUbiRateService tUbiRateService;

    /**
     * 查询UBI基础费率配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TUbiRate tUbiRate)
    {
        QueryWrapper<TUbiRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<TUbiRate> page = new Page<TUbiRate>(PageUtils.getPageNum(),PageUtils.getPageSize());
        Page<TUbiRate> pageList = tUbiRateService.page(page,queryWrapper);
        return getDataTable(pageList);
    }

    /**
     * 导出UBI基础费率配置列表
     */
    @Log(title = "UBI基础费率配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TUbiRate tUbiRate)
    {
        List<TUbiRate> list = tUbiRateService.selectTUbiRateList(tUbiRate);
        ExcelUtil<TUbiRate> util = new ExcelUtil<TUbiRate>(TUbiRate.class);
        util.exportExcel(response, list, "UBI基础费率配置数据");
    }

    /**
     * 获取UBI基础费率配置详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tUbiRateService.getById(id));
    }

    /**
     * 新增UBI基础费率配置
     */
    @Log(title = "UBI基础费率配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TUbiRate tUbiRate)
    {
        return toAjax(tUbiRateService.save(tUbiRate));
    }

    /**
     * 修改UBI基础费率配置
     */
    @Log(title = "UBI基础费率配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TUbiRate tUbiRate)
    {
        return toAjax(tUbiRateService.updateById(tUbiRate));
    }

    /**
     * 删除UBI基础费率配置
     */
    @Log(title = "UBI基础费率配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tUbiRateService.deleteTUbiRateByIds(ids));
    }
}
