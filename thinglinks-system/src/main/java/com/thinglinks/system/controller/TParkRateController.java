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
import com.thinglinks.system.domain.TParkRate;
import com.thinglinks.system.service.ITParkRateService;
import com.thinglinks.common.utils.poi.ExcelUtil;
import com.thinglinks.common.core.page.TableDataInfo;

/**
 * 停车费率配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/system/rate/park")
public class TParkRateController extends BaseController
{
    @Autowired
    private ITParkRateService tParkRateService;

    /**
     * 查询停车费率配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TParkRate tParkRate)
    {
        QueryWrapper<TParkRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<TParkRate> page = new Page<TParkRate>(PageUtils.getPageNum(),PageUtils.getPageSize());
        Page<TParkRate> pageList = tParkRateService.page(page,queryWrapper);
        return getDataTable(pageList);
    }

    /**
     * 导出停车费率配置列表
     */
    @Log(title = "停车费率配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TParkRate tParkRate)
    {
        List<TParkRate> list = tParkRateService.selectTParkRateList(tParkRate);
        ExcelUtil<TParkRate> util = new ExcelUtil<TParkRate>(TParkRate.class);
        util.exportExcel(response, list, "停车费率配置数据");
    }

    /**
     * 获取停车费率配置详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tParkRateService.getById(id));
    }

    /**
     * 新增停车费率配置
     */
    @Log(title = "停车费率配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TParkRate tParkRate)
    {
        return toAjax(tParkRateService.save(tParkRate));
    }

    /**
     * 修改停车费率配置
     */
    @Log(title = "停车费率配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TParkRate tParkRate)
    {
        return toAjax(tParkRateService.updateById(tParkRate));
    }

    /**
     * 删除停车费率配置
     */
    @Log(title = "停车费率配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tParkRateService.deleteTParkRateByIds(ids));
    }
}
