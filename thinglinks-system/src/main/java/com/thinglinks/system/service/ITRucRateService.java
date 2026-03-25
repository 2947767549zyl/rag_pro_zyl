package com.thinglinks.system.service;

import java.util.List;
import com.thinglinks.system.domain.TRucRate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * RUC费率配置Service接口
 *
 * @author ruoyi
 * @date 2026-03-16
 */
public interface ITRucRateService extends IService<TRucRate>
{
    /**
     * 查询RUC费率配置
     *
     * @param id RUC费率配置主键
     * @return RUC费率配置
     */
    public TRucRate selectTRucRateById(Long id);

    /**
     * 查询RUC费率配置列表
     *
     * @param tRucRate RUC费率配置
     * @return RUC费率配置集合
     */
    public List<TRucRate> selectTRucRateList(TRucRate tRucRate);

    /**
     * 新增RUC费率配置
     *
     * @param tRucRate RUC费率配置
     * @return 结果
     */
    public int insertTRucRate(TRucRate tRucRate);

    /**
     * 修改RUC费率配置
     *
     * @param tRucRate RUC费率配置
     * @return 结果
     */
    public int updateTRucRate(TRucRate tRucRate);

    /**
     * 批量删除RUC费率配置
     *
     * @param ids 需要删除的RUC费率配置主键集合
     * @return 结果
     */
    public int deleteTRucRateByIds(Long[] ids);

    /**
     * 删除RUC费率配置信息
     *
     * @param id RUC费率配置主键
     * @return 结果
     */
    public int deleteTRucRateById(Long id);
}
