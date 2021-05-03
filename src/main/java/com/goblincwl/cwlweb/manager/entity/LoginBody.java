package com.goblincwl.cwlweb.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录表单 Entity
 *
 * @author ☪wl
 * @date 2021-05-03 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody implements Serializable {

    /**
     * 密码
     */
    private String password;

}
