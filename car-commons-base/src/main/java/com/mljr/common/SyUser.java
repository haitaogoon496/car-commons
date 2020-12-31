package com.mljr.common;

import com.lyqc.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
@Data
public class SyUser extends BaseEntity {
    private Integer userId;

    private String userName;

    private String trueName;

    private Short cardType;

    private String cardId;

    private String password;

    private String birthday;

    private String sex;

    private String province;

    private String city;

    private String email;

    private String phone;

    private String address;

    private String postalCode;

    private String head;

    private Date createTime;

    private Date lastTime;

    private Integer loginTimes;

    private String userStatus;

    private String userType;
}