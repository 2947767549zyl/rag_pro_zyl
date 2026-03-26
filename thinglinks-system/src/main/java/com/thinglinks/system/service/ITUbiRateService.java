package com.thinglinks.system.service;

import java.util.List;
import com.thinglinks.system.domain.TUbiRate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * UBI基础费率配置Service接口
 *
 * @author ruoyi
 * @date 2026-03-16
 */
public interface ITUbiRateService extends IService<TUbiRate>
{
    /**
     * 查询UBI基础费率配置
     *
     * @param id UBI基础费率配置主键
     * @return UBI基础费率配置
     */
    public TUbiRate selectTUbiRateById(Long id);

    /**
     * 查询UBI基础费率配置列表
     *
     * @param tUbiRate UBI基础费率配置
     * @return UBI基础费率配置集合
     */
    public List<TUbiRate> selectTUbiRateList(TUbiRate tUbiRate);

    /**
     * 新增UBI基础费率配置
     *
     * @param tUbiRate UBI基础费率配置
     * @return 结果
     */
    public int insertTUbiRate(TUbiRate tUbiRate);

    /**
     * 修改UBI基础费率配置
     *
     * @param tUbiRate UBI基础费率配置
     * @return 结果
     */
    public int updateTUbiRate(TUbiRate tUbiRate);

    /**
     * 批量删除UBI基础费率配置
     *
     * @param ids 需要删除的UBI基础费率配置主键集合
     * @return 结果
     */
    public int deleteTUbiRateByIds(Long[] ids);

    /**
     * 删除UBI基础费率配置信息
     *
     * @param id UBI基础费率配置主键
     * @return 结果
     */
    public int deleteTUbiRateById(Long id);
}
