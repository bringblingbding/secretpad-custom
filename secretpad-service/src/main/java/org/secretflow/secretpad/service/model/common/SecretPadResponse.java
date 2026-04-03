/*
 * Copyright 2023 Ant Group Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.secretflow.secretpad.service.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * SecretPad common response
 *
 * @author yansi
 * @date 2023/5/10
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SecretPadResponse<T> {
    @Schema(description = "status information")
    private SecretPadResponseStatus status;
    @Schema
    private T data;

    /**
     * Build successful secretPad response with data
     *
     * @param data return data
     * @param <T>
     * @return successful secretPad response with data
     */
    public static <T> SecretPadResponse<T> success(T data) {
        return new SecretPadResponse<>(new SecretPadResponseStatus(0, "success"), data);
    }

    /**
     * Build successful SecretPad response
     *
     * @param <T>
     * @return successful SecretPad response
     */
    public static <T> SecretPadResponse<T> success() {
        return success(null);
    }

    /**
     * SecretPad response status class
 * 这是一个用于表示SecretPad响应状态的静态内部类
     */
    @Builder // 使用Builder模式构建对象，提供链式调用构建方法
    @Getter // 使用Lombok自动生成getter方法
    @AllArgsConstructor // 使用Lombok生成全参数构造方法
    @NoArgsConstructor // 使用Lombok生成无参构造方法
    @ToString // 使用Lombok自动生成toString方法
    public static class SecretPadResponseStatus {
        /**
         * Status code
     * 表示响应状态码，用于标识请求的处理结果
         */
        @Schema(description = "status code") // 使用Schema注解描述API文档中的字段含义
        private Integer code;
        /**
         * Status message
     * 表示响应状态信息，用于描述具体的处理结果或错误信息
         */
        @Schema(description = "status message") // 使用Schema注解描述API文档中的字段含义
        private String msg;
    }
}
