/*
 * Copyright (c) 2018 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.javaer.wechat.pay.model.notify;

import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.model.base.Coupon;
import cn.javaer.wechat.pay.support.SignIgnore;
import cn.javaer.wechat.pay.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 微信支付-支付结果通知.
 *
 * @author cn-src
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"coupons"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class PaymentNotify extends BasePayResponse {

    @XmlElement(name = "device_info")
    private String deviceInfo;

    @XmlElement(name = "openid")
    private String openId;

    @XmlElement(name = "is_subscribe")
    private String isSubscribe;

    @XmlElement(name = "trade_type")
    private String tradeType;

    @XmlElement(name = "bank_type")
    private String bankType;

    @XmlElement(name = "total_fee")
    private Integer totalFee;

    @XmlElement(name = "settlement_total_fee")
    private Integer settlementTotalFee;

    @XmlElement(name = "fee_type")
    private String feeType;

    @XmlElement(name = "cash_fee")
    private Integer cashFee;

    @XmlElement(name = "cash_fee_type")
    private String cashFeeType;

    @XmlElement(name = "coupon_fee")
    private Integer couponFee;

    @XmlElement(name = "coupon_count")
    private Integer couponCount;

    @XmlElement(name = "transaction_id")
    private String transactionId;

    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "attach")
    private String attach;

    @XmlElement(name = "time_end")
    private String timeEnd;

    /**
     * 代金券.
     */
    @SignIgnore
    private List<Coupon> coupons;

    @Override
    public void subProcessResponse() {
        if (null == this.coupons && null != this.otherParams) {
            this.coupons = ObjectUtils.couponsFrom(this.otherParams);
        }
    }
}
