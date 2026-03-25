package com.thinglinks.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.thinglinks.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * RUC费率配置对象 t_ruc_rate
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_ruc_rate")
public class TRucRate
{
private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    /** 道路等级（高速/国道/省道/市区） */
    @Excel(name = "道路等级", readConverterExp = "高=速/国道/省道/市区")
    private String roadLevel;
    /** 单位里程（公里） */
    @Excel(name = "单位里程", readConverterExp = "公=里")
    private Long unitMileage;
    /** 单价（元） */
    @Excel(name = "单价", readConverterExp = "元=")
    private BigDecimal unitPrice;
    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;
    /** 失效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "失效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;
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
