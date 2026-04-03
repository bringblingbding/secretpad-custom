package org.secretflow.secretpad.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "digital_contract")
@Getter
@Setter
public class DigitalContractDO {

    /**
     * 数字合约唯一id，作为主键，不可为空
     */
    @Id
    @Column(name = "contract_id", nullable = false, length = 255)
    private String contractId;

    /**
     * 合约名称
     */
    @Column(name = "contract_name", length = 255)
    private String contractName;

    /**
     * 合约简介
     */
    @Column(name = "contract_abstract", length = 255)
    private String contractAbstract;

    /**
     * 创建时间
     * 数据库定义了 ON UPDATE CURRENT_TIMESTAMP，JPA中通常由应用层控制或标记为 @Column(insertable=false, updatable=false)
     */
    @Column(name = "issue_time")
    private LocalDateTime issueTime;

    /**
     * 生效时间
     */
    @Column(name = "activation_time")
    private LocalDateTime activationTime;

    /**
     * 终止时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 备案时间
     */
    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    /**
     * 解除时间
     */
    @Column(name = "terminate_time")
    private LocalDateTime terminateTime;

    /**
     * 解除类型，1代表提前解除，2代表到期解除
     */
    @Column(name = "terminate_type", length = 255)
    private String terminateType;

    /**
     * 发起模式，1代表点对点协商，2代表中介参与协商
     */
    @Column(name = "sign_mode", length = 255)
    private String signMode;

    /**
     * 数据产品唯一id（关联数据产品表，查询数据提供方信息）
     */
    @Column(name = "data_product_id")
    private Integer dataProductId;

    /**
     * 策略id
     */
    @Column(name = "strategy_id")
    private Integer strategyId;

    /**
     * 签署时间
     */
    @Column(name = "signatory_time")
    private LocalDateTime signatoryTime;

    /**
     * 数字合约状态（1.协商中、3.签订中、4.待备案、5.履行中（已备案）、6.终止）
     */
    @Column(name = "contract_status", length = 255)
    private String contractStatus;

    /**
     * 提供方对合约的签署状态
     */
    @Column(name = "provider_signature", length = 255)
    private String providerSignature;

    /**
     * 签署状态 (0:未签, 1:已签, 2:拒签)
     */
    @Column(name = "provider_sign_status")
    private Integer providerSignStatus;

    /**
     * 使用方签署状态(0:未签, 1:已签, 2:拒签)
     * 注意：数据库定义为 varchar，故此处使用 String
     */
    @Column(name = "consumer_sign_status", length = 255)
    private String consumerSignStatus;

    /**
     * 合约哈希值，用于审计与存证（防篡改）
     */
    @Column(name = "contract_hash", length = 255)
    private String contractHash;

    /**
     * 用于存储附件信息、补充条款等。
     */
    @Column(name = "expansion_item", length = 255)
    private String expansionItem;

    /**
     * 平台方id
     */
    @Column(name = "platform_id", length = 255)
    private String platformId;

    /**
     * 合约版本号
     */
    @Column(name = "contract_version", length = 255)
    private String contractVersion;

    /**
     * 提供方用户id
     */
    @Column(name = "provider_user_id")
    private Integer providerUserId;

    /**
     * 使用方用户id
     */
    @Column(name = "consumer_user_id")
    private Integer consumerUserId;

    /**
     * 当前处理的角色（当 A 发送给 B 时，把这个字段改为 B）
     */
    @Column(name = "current_handler_role", length = 255)
    private String currentHandlerRole;
}