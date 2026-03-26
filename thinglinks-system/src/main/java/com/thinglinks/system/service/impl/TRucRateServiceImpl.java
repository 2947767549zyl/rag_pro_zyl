package com.thinglinks.system.service.impl;

import java.util.List;
        import com.thinglinks.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinglinks.system.mapper.TRucRateMapper;
import com.thinglinks.system.domain.TRucRate;
import com.thinglinks.system.service.ITRucRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * RUC费率配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Service
public class TRucRateServiceImpl extends ServiceImpl<TRucRateMapper, TRucRate> implements ITRucRateService
{
    @Autowired
    private TRucRateMapper tRucRateMapper;

    /**
     * 查询RUC费率配置
     *
     * @param id RUC费率配置主键
     * @return RUC费率配置
     */
    @Override
    public TRucRate selectTRucRateById(Long id)
    {
        return tRucRateMapper.selectTRucRateById(id);
    }

    /**
     * 查询RUC费率配置列表
     *
     * @param tRucRate RUC费率配置
     * @return RUC费率配置
     */
    @Override
    public List<TRucRate> selectTRucRateList(TRucRate tRucRate)
    {
        return tRucRateMapper.selectTRucRateList(tRucRate);
    }

    /**
     * 新增RUC费率配置
     *
     * @param tRucRate RUC费率配置
     * @return 结果
     */
    @Override
    public int insertTRucRate(TRucRate tRucRate)
    {
        tRucRate.setCreateTime(DateUtils.getNowDate());
        return tRucRateMapper.insertTRucRate(tRucRate);
    }

    /**
     * 修改RUC费率配置
     *
     * @param tRucRate RUC费率配置
     * @return 结果
     */
    @Override
    public int updateTRucRate(TRucRate tRucRate)
    {
                tRucRate.setUpdateTime(DateUtils.getNowDate());
        return tRucRateMapper.updateTRucRate(tRucRate);
    }

    /**
     * 批量删除RUC费率配置
     *
     * @param ids 需要删除的RUC费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTRucRateByIds(Long[] ids)
    {
        return tRucRateMapper.deleteTRucRateByIds(ids);
    }

    /**
     * 删除RUC费率配置信息
     *
     * @param id RUC费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTRucRateById(Long id)
    {
        return tRucRateMapper.deleteTRucRateById(id);
    }
}
