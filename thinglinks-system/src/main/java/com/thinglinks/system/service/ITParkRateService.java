package com.thinglinks.system.service;

import java.util.List;
import com.thinglinks.system.domain.TParkRate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 停车费率配置Service接口
 *
 * @author ruoyi
 * @date 2026-03-16
 */
public interface ITParkRateService extends IService<TParkRate>
{
    /**
     * 查询停车费率配置
     *
     * @param id 停车费率配置主键
     * @return 停车费率配置
     */
    public TParkRate selectTParkRateById(Long id);

    /**
     * 查询停车费率配置列表
     *
     * @param tParkRate 停车费率配置
     * @return 停车费率配置集合
     */
    public List<TParkRate> selectTParkRateList(TParkRate tParkRate);

    /**
     * 新增停车费率配置
     *
     * @param tParkRate 停车费率配置
     * @return 结果
     */
    public int insertTParkRate(TParkRate tParkRate);

    /**
     * 修改停车费率配置
     *
     * @param tParkRate 停车费率配置
     * @return 结果
     */
    public int updateTParkRate(TParkRate tParkRate);

    /**
     * 批量删除停车费率配置
     *
     * @param ids 需要删除的停车费率配置主键集合
     * @return 结果
     */
    public int deleteTParkRateByIds(Long[] ids);

    /**
     * 删除停车费率配置信息
     *
     * @param id 停车费率配置主键
     * @return 结果
     */
    public int deleteTParkRateById(Long id);
}
