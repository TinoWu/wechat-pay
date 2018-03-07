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

package cn.javaer.wechat.pay.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付-退款结果通知.
 *
 * @author zhangpeng
 */
@Getter
@Setter
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class RefundNotifyResult extends BasePayResponse {

    /**
     * 加密信息.
     */
    @XmlElement(name = "req_info")
    private String reqInfo;

    @XmlElement(name = "transaction_id")
    private String transactionId;

    @XmlElement(name = "out_trade_no")
    private String outTradeNo;

    @XmlElement(name = "refund_id")
    private String refundId;

    @XmlElement(name = "out_refund_no")
    private String outRefundNo;

    @XmlElement(name = "total_fee")
    private Integer totalFee;

    @XmlElement(name = "settlement_total_fee")
    private Integer settlementTotalFee;

    @XmlElement(name = "refund_fee")
    private Integer refundFee;

    @XmlElement(name = "settlement_refund_fee")
    private Integer settlementRefundFee;

    @XmlElement(name = "refund_status")
    private String refundStatus;

    @XmlElement(name = "success_time")
    private String successTime;

    @XmlElement(name = "refund_recv_accout")
    private String refundRecvAccout;

    @XmlElement(name = "refund_account")
    private String refundAccount;

    @XmlElement(name = "refund_request_source")
    private String refundRequestSource;

    @Override
    public void beforeSign() {
        // 解密字段
    }
}