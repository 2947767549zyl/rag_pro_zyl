package com.thinglinks.system.service.impl;

import java.util.List;
        import com.thinglinks.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinglinks.system.mapper.TParkRateMapper;
import com.thinglinks.system.domain.TParkRate;
import com.thinglinks.system.service.ITParkRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 停车费率配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Service
public class TParkRateServiceImpl extends ServiceImpl<TParkRateMapper, TParkRate> implements ITParkRateService
{
    @Autowired
    private TParkRateMapper tParkRateMapper;

    /**
     * 查询停车费率配置
     *
     * @param id 停车费率配置主键
     * @return 停车费率配置
     */
    @Override
    public TParkRate selectTParkRateById(Long id)
    {
        return tParkRateMapper.selectTParkRateById(id);
    }

    /**
     * 查询停车费率配置列表
     *
     * @param tParkRate 停车费率配置
     * @return 停车费率配置
     */
    @Override
    public List<TParkRate> selectTParkRateList(TParkRate tParkRate)
    {
        return tParkRateMapper.selectTParkRateList(tParkRate);
    }

    /**
     * 新增停车费率配置
     *
     * @param tParkRate 停车费率配置
     * @return 结果
     */
    @Override
    public int insertTParkRate(TParkRate tParkRate)
    {
        tParkRate.setCreateTime(DateUtils.getNowDate());
        return tParkRateMapper.insertTParkRate(tParkRate);
    }

    /**
     * 修改停车费率配置
     *
     * @param tParkRate 停车费率配置
     * @return 结果
     */
    @Override
    public int updateTParkRate(TParkRate tParkRate)
    {
                tParkRate.setUpdateTime(DateUtils.getNowDate());
        return tParkRateMapper.updateTParkRate(tParkRate);
    }

    /**
     * 批量删除停车费率配置
     *
     * @param ids 需要删除的停车费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTParkRateByIds(Long[] ids)
    {
        return tParkRateMapper.deleteTParkRateByIds(ids);
    }

    /**
     * 删除停车费率配置信息
     *
     * @param id 停车费率配置主键
     * @return 结果
     */
    @Override
    public int deleteTParkRateById(Long id)
    {
        return tParkRateMapper.deleteTParkRateById(id);
    }
}
