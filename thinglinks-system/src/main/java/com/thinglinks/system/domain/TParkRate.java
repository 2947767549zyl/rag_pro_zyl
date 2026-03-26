package com.thinglinks.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.thinglinks.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 停车费率配置对象 t_park_rate
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_park_rate")
public class TParkRate
{
private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    /** 停车场名称 */
    @Excel(name = "停车场名称")
    private String parkName;
    /** 免费时长（分钟） */
    @Excel(name = "免费时长", readConverterExp = "分=钟")
    private Long freeTime;
    /** 单位时长（分钟） */
    @Excel(name = "单位时长", readConverterExp = "分=钟")
    private Long unitTime;
    /** 单价（元） */
    @Excel(name = "单价", readConverterExp = "元=")
    private BigDecimal unitPrice;
    /** 单日封顶金额 */
    @Excel(name = "单日封顶金额")
    private BigDecimal dayMaxPrice;
    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;
    /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
