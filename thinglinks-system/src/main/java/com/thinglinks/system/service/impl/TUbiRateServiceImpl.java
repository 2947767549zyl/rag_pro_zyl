package com.thinglinks.system.service.impl;

import java.util.List;
        import com.thinglinks.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinglinks.system.mapper.TUbiRateMapper;
import com.thinglinks.system.domain.TUbiRate;
import com.thinglinks.system.service.ITUbiRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * UBI基础费率配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Service
public class TUbiRateServiceImpl extends ServiceImpl<TUbiRateMapper, TUbiRate> implements ITUbiRateService
{
    @Autowired
    private TUbiRateMapper tUbiRateMapper;

    /**
     * 查询UBI基础费率配置
     *
     * @param id UBI基础费率配置主键
     * @return UBI基础费率配置
     */
    @Override
    public TUbiRate selectTUbiRateById(Long id)
    {
        return tUbiRateMapper.selectTUbiRateById(id);
    }

    /**
     * 查询UBI基础费率配置列表
     *
     * @param tUbiRate UBI基础费率配置
     * @return UBI基础费率配置
     */
    @Override
    public List<TUbiRate> selectTUbiRateList(TUbiRate tUbiRate)
    {
        return tUbiRateMapper.selectTUbiRateList(tUbiRate);
    }

    /**
     * 新增UBI基础费率配置
     *
     * @param tUbiRate UBI基础费率配置
     * @return 结果
     */
    @Override
    public int insertTUbiRate(TUbiRate tUbiRate)
    {
        tUbiRate.setCreateTime(DateUtils.getNowDate());
        return tUbiRateMapper.insertTUbiRate(tUbiRate);
    }

    /**
     * 修改UBI基础费率配置
     *
     * @param tUbiRate UBI基础费率配置
     * @return 结果
     */
    @Override
    public int updateTUbiRate(TUbiRate tUbiRate)
    {
                tUbiRate.setUpdateTime(DateUtils.getNowDate());
        return tUbiRateMapper.updateTUbiRate(tUbiRate);
    }

    /**
     * 批量删除UBI基础费率配置
     *
     * @param ids 需要删除的UBI基础费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTUbiRateByIds(Long[] ids)
    {
        return tUbiRateMapper.deleteTUbiRateByIds(ids);
    }

    /**
     * 删除UBI基础费率配置信息
     *
     * @param id UBI基础费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTUbiRateById(Long id)
    {
        return tUbiRateMapper.deleteTUbiRateById(id);
    }
}
