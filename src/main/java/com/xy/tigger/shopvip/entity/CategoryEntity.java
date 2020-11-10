package com.xy.tigger.shopvip.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-16 16:52:23
 */
@Data
@TableName("ps_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父id
	 */
	private Integer parentId;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 是否可见
	 */
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;

	@TableField(exist = false)
	private List<CategoryEntity> Chidrens;

	private String icon;

	private Integer aStatus;

}
