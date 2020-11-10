package com.xy.tigger.shopvip.entity;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date 2020-08-16 16:52:23
 */
@Data
@TableName("ps_order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 订单号
	 */
	private String oCode;
	/**
	 * 用户id
	 */
	private Integer uId;
	/**
	 * 商品id
	 */
	private Integer cId;
	/**
	 * 金额
	 */
	private BigDecimal oMoney;
	/**
	 * 商品数量
	 */
	private Integer numbers;
	/**
	 * 用户收货地址id
	 */
	private Integer uAddress;
	/**
	 * 支付状态（1.未支付，2.已支付，3.商家已发货，4，订单完成）
	 */
	private Integer payStatus;
	/**
	 * 是否显示（1显示，0不显示）
	 */
	private Integer showStatus;


	private Date creatTime;

	private Integer payType;

	private String productId;

	private BigDecimal postage;

	private BigDecimal point;

	private BigDecimal pRealMoney;

	private BigDecimal pBalance;

}
