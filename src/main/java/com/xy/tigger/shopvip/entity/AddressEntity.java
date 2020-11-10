package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("ps_address")
public class AddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer uId;
	/**
	 * 用户收货地址
	 */
	private String uAddress;
	/**
	 * 是否显示（1显示，0不显示）
	 */
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;

	private String iphone;

	private String username;

}
