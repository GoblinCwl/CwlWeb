package com.goblincwl.cwlweb.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 键值对配置 Entity
 *
 * @author ☪wl
 * @date 2021-05-03 14:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueOptions implements Serializable {

    private static final long serialVersionUID = 180169777775273753L;

    /**
     * 键
     */
    @TableId(type = IdType.NONE)
    private String optKey;
    /**
     * 值
     */
    private String optValue;
}
