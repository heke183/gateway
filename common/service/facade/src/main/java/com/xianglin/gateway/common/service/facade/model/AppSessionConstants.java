package com.xianglin.gateway.common.service.facade.model;

import java.io.Serializable;

public interface AppSessionConstants extends Serializable {

    /**
     * 登陆用户partyId
     * Long 类型
     */
    public static final String PARTY_ID = "partyId";

    /**
     * 设备号
     * String
     */
    public static final String DEVICE_ID = "did";
    /**
     * 用户开通业务类型
     * Set<String>类型
     * 暂时兼容，不推荐使用
     */
    @Deprecated
    public static final String BUSINESS_OPEN_INFO = "businessOpenInfo";

    /** 登录账户
     * 用户手机号
     * String
     */
    public static final String LOGIN_NAME = "loginName";
}
