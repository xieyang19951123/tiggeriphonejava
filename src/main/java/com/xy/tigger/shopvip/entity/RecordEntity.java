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
 * @date 2020-08-16 16:52:23
 */
@Data
@TableName("ps_record")
public class RecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 学生id
	 */
	private Integer uId;
	/**
	 * 文本
	 */
	private String message;
	/**
	 * 是否显示1显示，0不显示
	 */
	private Integer showStatus;
	/**
	 * 商家id
	 */
	private Integer boosId;

}
