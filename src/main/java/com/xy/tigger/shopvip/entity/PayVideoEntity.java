package com.xy.tigger.shopvip.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-29 16:27:53
 */
@Data
@TableName("ps_pay_video")
public class PayVideoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 分类id
	 */
	private String cid;
	/**
	 * 使用的积分
	 */
	private BigDecimal point;
	/**
	 * 附件id
	 */
	private Integer fjId;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Integer sales;
	/**
	 * 视频介绍
	 */
	private String introduce;

	/**
	 * 是否显示1显示，0不显示
	 */
	@TableLogic(value = "1" ,delval = "0")
	private Integer showStatus;
	/**
	 * 是否主页显示
	 */
	private Integer homeShow;


	private String videoId;

	private Date careatTime;

	private Integer oid;

	@TableField(exist = false)
	private String auth;

	@TableField(exist =false)
	private OrganEntity organEntity;


	private Integer sharePoint;

	private Integer byPoint;

	private Integer byPointFather;




}
