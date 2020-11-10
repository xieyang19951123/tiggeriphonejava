package com.xy.tigger.shopvip.entity;
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
 * @date 2020-08-16 16:52:24
 */
@Data
@TableName("ps_comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 商品名称
	 */
	private String comName;
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
	private String fjId;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Integer sales;

	private Integer cover;

	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;

	private Integer homeShow;

	private BigDecimal postage;

	private Integer sharePoint;

	private Integer byPoint;

	private Integer byPointFather;

	private String address;



}
