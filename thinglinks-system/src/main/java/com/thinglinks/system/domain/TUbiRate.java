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
 * UBI基础费率配置对象 t_ubi_rate
 *
 * @author ruoyi
 * @date 2026-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_ubi_rate")
public class TUbiRate
{
private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    /** 车型（客车/货车/网约车/私家车） */
    @Excel(name = "车型", readConverterExp = "客=车/货车/网约车/私家车")
    private String carType;
    /** 基础保费 */
    @Excel(name = "基础保费")
    private BigDecimal basePrice;
    /** 最高保费 */
    @Excel(name = "最高保费")
    private BigDecimal maxPrice;
    /** 最低保费 */
    @Excel(name = "最低保费")
    private BigDecimal minPrice;
    /** 风险系数 */
    @Excel(name = "风险系数")
    private BigDecimal riskCoefficient;
    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;
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
