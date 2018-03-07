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

package sample;

import cn.javaer.wechat.pay.model.DownloadBillRequest;

import java.time.LocalDate;

/**
 * @author zhangpeng
 */
public class DownloadBillSample {
    public static void main(final String[] args) throws Exception {
        WeChatPayClientFactory.init();
        final DownloadBillRequest request = DownloadBillRequest.create(LocalDate.now().minusDays(1), DownloadBillRequest.BillType.ALL);
        final byte[] response = WeChatPayClientFactory.client().downloadBill(request);
        System.out.println(new String(response, "UTF-8"));
    }
}