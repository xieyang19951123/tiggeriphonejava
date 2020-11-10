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
@TableName("ps_accessory")
public class AccessoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 商品名称
	 */
	private String aSrc;
	/**
	 * 附件类型（1.图片，2视频）
	 */
	private Integer aStatus;
	/**
	 * 是否显示（1显示，0不显示）
	 */
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;

	private String fileName;


	private String coverSrc;

	private Date createTime;

}
