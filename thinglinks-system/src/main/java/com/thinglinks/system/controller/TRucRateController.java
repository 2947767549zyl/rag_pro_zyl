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
import com.thinglinks.system.domain.TRucRate;
import com.thinglinks.system.service.ITRucRateService;
import com.thinglinks.common.utils.poi.ExcelUtil;
import com.thinglinks.common.core.page.TableDataInfo;

/**
 * RUC费率配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/system/rate/ruc")
public class TRucRateController extends BaseController
{
    @Autowired
    private ITRucRateService tRucRateService;

    /**
     * 查询RUC费率配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TRucRate tRucRate)
    {
        QueryWrapper<TRucRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<TRucRate> page = new Page<TRucRate>(PageUtils.getPageNum(),PageUtils.getPageSize());
        Page<TRucRate> pageList = tRucRateService.page(page,queryWrapper);
        return getDataTable(pageList);
    }

    /**
     * 导出RUC费率配置列表
     */
    @Log(title = "RUC费率配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRucRate tRucRate)
    {
        List<TRucRate> list = tRucRateService.selectTRucRateList(tRucRate);
        ExcelUtil<TRucRate> util = new ExcelUtil<TRucRate>(TRucRate.class);
        util.exportExcel(response, list, "RUC费率配置数据");
    }

    /**
     * 获取RUC费率配置详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tRucRateService.getById(id));
    }

    /**
     * 新增RUC费率配置
     */
    @Log(title = "RUC费率配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRucRate tRucRate)
    {
        return toAjax(tRucRateService.save(tRucRate));
    }

    /**
     * 修改RUC费率配置
     */
    @Log(title = "RUC费率配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRucRate tRucRate)
    {
        return toAjax(tRucRateService.updateById(tRucRate));
    }

    /**
     * 删除RUC费率配置
     */
    @Log(title = "RUC费率配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tRucRateService.deleteTRucRateByIds(ids));
    }
}
