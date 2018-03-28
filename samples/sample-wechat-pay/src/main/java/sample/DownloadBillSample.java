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

import cn.javaer.wechat.pay.model.DownloadBillResponse;
import cn.javaer.wechat.pay.model.base.BillType;

import java.time.LocalDate;

/**
 * @author zhangpeng
 */
public class DownloadBillSample {
    public static void main(final String[] args) throws Exception {
        final DownloadBillResponse response = WeChatPayServiceFactory.weChatPayService().downloadBill(LocalDate.of(2018, 3, 7), BillType.ALL);
        System.out.println(response);
    }
}
