package com.xy.tigger.shopvip.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("ps_sign")
public class SignEntity implements Serializable {
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
	 * 签到时间
	 */
	private String signTime;
	/**
	 * 是否显示（1显示，0不显示）
	 */
	private Integer showStatus;

}
