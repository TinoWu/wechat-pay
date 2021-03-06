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

package cn.javaer.wechat.pay.client;

import cn.javaer.wechat.pay.model.CloseOrderRequest;
import cn.javaer.wechat.pay.model.CloseOrderResponse;
import cn.javaer.wechat.pay.model.DownloadBillRequest;
import cn.javaer.wechat.pay.model.DownloadBillResponse;
import cn.javaer.wechat.pay.model.OrderQueryRequest;
import cn.javaer.wechat.pay.model.OrderQueryResponse;
import cn.javaer.wechat.pay.model.RefundQueryRequest;
import cn.javaer.wechat.pay.model.RefundQueryResponse;
import cn.javaer.wechat.pay.model.RefundRequest;
import cn.javaer.wechat.pay.model.RefundResponse;
import cn.javaer.wechat.pay.model.UnifiedOrderRequest;
import cn.javaer.wechat.pay.model.UnifiedOrderResponse;
import cn.javaer.wechat.pay.model.base.BasePayResponse;
import cn.javaer.wechat.pay.util.CodecUtils;
import cn.javaer.wechat.pay.util.ObjectUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static cn.javaer.wechat.pay.util.ObjectUtils.checkNotEmpty;
import static cn.javaer.wechat.pay.util.ObjectUtils.checkNotNull;

/**
 * 微信支付客户端-RestTemplate实现.
 *
 * @author cn-src
 */
public class WeChatPayHttpComponentsClient implements WeChatPayClient {
    private static final String XML_TAG = "<xml>";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final HttpClient httpClient;
    private final String basePath;

    /**
     * Instantiates a new WeChatPayHttpComponentsClient.
     *
     * @param apiBasePath apiBasePath
     * @param httpClient httpClient
     */
    public WeChatPayHttpComponentsClient(final String apiBasePath, final HttpClient httpClient) {
        checkNotEmpty(apiBasePath, "apiBasePath");
        checkNotNull(httpClient, "httpClient");

        this.basePath = apiBasePath;
        this.httpClient = httpClient;
    }

    /**
     * Instantiates a new WeChatPayHttpComponentsClient.
     *
     * @param httpClient httpClient
     */
    public WeChatPayHttpComponentsClient(final HttpClient httpClient) {
        checkNotNull(httpClient, "httpClient");

        this.basePath = WeChatPayClient.BASE_PATH;
        this.httpClient = httpClient;
    }

    @Override
    public UnifiedOrderResponse unifiedOrder(final UnifiedOrderRequest request) {
        checkNotNull(request, "UnifiedOrderRequest");
        return doPost(WeChatPayClient.UNIFIED_ORDER_PATH, request, UnifiedOrderResponse.class);
    }

    @Override
    public OrderQueryResponse orderQuery(final OrderQueryRequest request) {
        checkNotNull(request, "OrderQueryRequest");
        return doPost(WeChatPayClient.ORDER_QUERY_PATH, request, OrderQueryResponse.class);
    }

    @Override
    public CloseOrderResponse closeOrder(final CloseOrderRequest request) {
        checkNotNull(request, "CloseOrderRequest");
        return doPost(WeChatPayClient.CLOSE_ORDER_PATH, request, CloseOrderResponse.class);
    }

    @Override
    public RefundResponse refund(final RefundRequest request) {
        checkNotNull(request, "RefundRequest");
        return doPost(WeChatPayClient.REFUND_PATH, request, RefundResponse.class);
    }

    @Override
    public RefundQueryResponse refundQuery(final RefundQueryRequest request) {
        checkNotNull(request, "RefundQueryRequest");
        return doPost(WeChatPayClient.REFUND_QUERY_PATH, request, RefundQueryResponse.class);
    }

    @Override
    public DownloadBillResponse downloadBill(final DownloadBillRequest request) {
        checkNotNull(request, "DownloadBillRequest");
        final HttpPost httpPost = new HttpPost();
        try {
            final String apiUrl = ObjectUtils.fullApiUrl(this.basePath, WeChatPayClient.DOWNLOAD_BILL_PATH);
            httpPost.setURI(new URI(apiUrl));
            final String xmlReq = CodecUtils.marshal(request);
            this.log.debug("WeChat pay request({}):\n{}", WeChatPayClient.DOWNLOAD_BILL_PATH, xmlReq);
            httpPost.setEntity(new StringEntity(xmlReq, StandardCharsets.UTF_8));
            final HttpResponse httpResponse = this.httpClient.execute(httpPost);
            byte[] responseBytes = EntityUtils.toByteArray(httpResponse.getEntity());
            if (CodecUtils.isCompressed(responseBytes)) {
                responseBytes = CodecUtils.unCompress(responseBytes);
            }
            final String responseStr = new String(responseBytes, StandardCharsets.UTF_8);
            this.log.debug("WeChat pay response({}):\n{}", WeChatPayClient.DOWNLOAD_BILL_PATH, responseStr);
            if (responseStr.startsWith(XML_TAG)) {
                return CodecUtils.unmarshal(responseStr, DownloadBillResponse.class);
            }
            else {
                return ObjectUtils.billResponseFrom(responseStr);
            }
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Invalid 'url'", e);
        }
    }

    private <Q, S extends BasePayResponse> S doPost(
            final String apiPath, final Q request, final Class<S> responseClass) {

        final String responseStr;
        try {
            final HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(ObjectUtils.fullApiUrl(this.basePath, apiPath)));
            final String xmlReq = CodecUtils.marshal(request);
            this.log.debug("WeChat pay request({}):\n{}", apiPath, xmlReq);
            httpPost.setEntity(new StringEntity(xmlReq, StandardCharsets.UTF_8));
            responseStr = EntityUtils.toString(this.httpClient.execute(httpPost).getEntity(), StandardCharsets.UTF_8);
            this.log.debug("WeChat pay response({}):\n{}", apiPath, responseStr);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Invalid 'url'", e);
        }
        return CodecUtils.unmarshal(responseStr, responseClass);
    }
}
