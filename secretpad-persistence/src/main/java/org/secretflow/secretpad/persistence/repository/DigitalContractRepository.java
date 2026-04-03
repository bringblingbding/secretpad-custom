package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.DigitalContractDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DigitalContractRepository extends BaseRepository<DigitalContractDO, String> {

    /* ================== 新增 (全字段) ================== */
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO digital_contract ( " +
                    "  contract_id, contract_name, contract_abstract, issue_time, " +
                    "  activation_time, end_time, registration_time, terminate_time, " +
                    "  terminate_type, sign_mode, data_product_id, strategy_id, " +
                    "  signatory_time, contract_status, provider_signature, " +
                    "  provider_sign_status, consumer_sign_status, contract_hash, " +
                    "  expansion_item, platform_id, contract_version, " +
                    "  provider_user_id, consumer_user_id, current_handler_role " +
                    ") VALUES ( " +
                    "  :#{#dto.contractId}, :#{#dto.contractName}, :#{#dto.contractAbstract}, :#{#dto.issueTime}, " +
                    "  :#{#dto.activationTime}, :#{#dto.endTime}, :#{#dto.registrationTime}, :#{#dto.terminateTime}, " +
                    "  :#{#dto.terminateType}, :#{#dto.signMode}, :#{#dto.dataProductId}, :#{#dto.strategyId}, " +
                    "  :#{#dto.signatoryTime}, :#{#dto.contractStatus}, :#{#dto.providerSignature}, " +
                    "  :#{#dto.providerSignStatus}, :#{#dto.consumerSignStatus}, :#{#dto.contractHash}, " +
                    "  :#{#dto.expansionItem}, :#{#dto.platformId}, :#{#dto.contractVersion}, " +
                    "  :#{#dto.providerUserId}, :#{#dto.consumerUserId}, :#{#dto.currentHandlerRole} " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") DigitalContractDO dto);

    /* ================== 修改（全字段，空值忽略） ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE digital_contract SET " +
                    "  contract_name        = CASE WHEN :#{#dto.contractName}       IS NULL THEN contract_name        ELSE :#{#dto.contractName}       END, " +
                    "  contract_abstract    = CASE WHEN :#{#dto.contractAbstract}   IS NULL THEN contract_abstract    ELSE :#{#dto.contractAbstract}   END, " +
                    "  issue_time           = CASE WHEN :#{#dto.issueTime}          IS NULL THEN issue_time           ELSE :#{#dto.issueTime}          END, " +
                    "  activation_time      = CASE WHEN :#{#dto.activationTime}     IS NULL THEN activation_time      ELSE :#{#dto.activationTime}     END, " +
                    "  end_time             = CASE WHEN :#{#dto.endTime}            IS NULL THEN end_time             ELSE :#{#dto.endTime}            END, " +
                    "  registration_time    = CASE WHEN :#{#dto.registrationTime}   IS NULL THEN registration_time    ELSE :#{#dto.registrationTime}   END, " +
                    "  terminate_time       = CASE WHEN :#{#dto.terminateTime}      IS NULL THEN terminate_time       ELSE :#{#dto.terminateTime}      END, " +
                    "  terminate_type       = CASE WHEN :#{#dto.terminateType}      IS NULL THEN terminate_type       ELSE :#{#dto.terminateType}      END, " +
                    "  sign_mode            = CASE WHEN :#{#dto.signMode}           IS NULL THEN sign_mode            ELSE :#{#dto.signMode}           END, " +
                    "  data_product_id      = CASE WHEN :#{#dto.dataProductId}      IS NULL THEN data_product_id      ELSE :#{#dto.dataProductId}      END, " +
                    "  strategy_id          = CASE WHEN :#{#dto.strategyId}         IS NULL THEN strategy_id          ELSE :#{#dto.strategyId}         END, " +
                    "  signatory_time       = CASE WHEN :#{#dto.signatoryTime}      IS NULL THEN signatory_time       ELSE :#{#dto.signatoryTime}      END, " +
                    "  contract_status      = CASE WHEN :#{#dto.contractStatus}     IS NULL THEN contract_status      ELSE :#{#dto.contractStatus}     END, " +
                    "  provider_signature   = CASE WHEN :#{#dto.providerSignature}  IS NULL THEN provider_signature   ELSE :#{#dto.providerSignature}  END, " +
                    "  provider_sign_status = CASE WHEN :#{#dto.providerSignStatus} IS NULL THEN provider_sign_status ELSE :#{#dto.providerSignStatus} END, " +
                    "  consumer_sign_status = CASE WHEN :#{#dto.consumerSignStatus} IS NULL THEN consumer_sign_status ELSE :#{#dto.consumerSignStatus} END, " +
                    "  contract_hash        = CASE WHEN :#{#dto.contractHash}       IS NULL THEN contract_hash        ELSE :#{#dto.contractHash}       END, " +
                    "  expansion_item       = CASE WHEN :#{#dto.expansionItem}      IS NULL THEN expansion_item       ELSE :#{#dto.expansionItem}      END, " +
                    "  platform_id          = CASE WHEN :#{#dto.platformId}         IS NULL THEN platform_id          ELSE :#{#dto.platformId}         END, " +
                    "  contract_version     = CASE WHEN :#{#dto.contractVersion}    IS NULL THEN contract_version     ELSE :#{#dto.contractVersion}    END, " +
                    "  provider_user_id     = CASE WHEN :#{#dto.providerUserId}     IS NULL THEN provider_user_id     ELSE :#{#dto.providerUserId}     END, " +
                    "  consumer_user_id     = CASE WHEN :#{#dto.consumerUserId}     IS NULL THEN consumer_user_id     ELSE :#{#dto.consumerUserId}     END, " +
                    "  current_handler_role = CASE WHEN :#{#dto.currentHandlerRole} IS NULL THEN current_handler_role ELSE :#{#dto.currentHandlerRole} END " +
                    "WHERE contract_id = :#{#dto.contractId}",
            nativeQuery = true)
    int update(@Param("dto") DigitalContractDO dto);

    /* ================== 动态条件查询（全字段，空值忽略） ================== */
    @Query(value =
            "SELECT * FROM digital_contract " +
                    "WHERE (:#{#dto.contractId}         IS NULL OR contract_id         = :#{#dto.contractId}) " +
                    "  AND (:#{#dto.contractName}       IS NULL OR contract_name       LIKE CONCAT('%',:#{#dto.contractName},'%')) " +
                    "  AND (:#{#dto.contractAbstract}   IS NULL OR contract_abstract   LIKE CONCAT('%',:#{#dto.contractAbstract},'%')) " +
                    "  AND (:#{#dto.issueTime}          IS NULL OR issue_time          = :#{#dto.issueTime}) " +
                    "  AND (:#{#dto.activationTime}     IS NULL OR activation_time     = :#{#dto.activationTime}) " +
                    "  AND (:#{#dto.endTime}            IS NULL OR end_time            = :#{#dto.endTime}) " +
                    "  AND (:#{#dto.registrationTime}   IS NULL OR registration_time   = :#{#dto.registrationTime}) " +
                    "  AND (:#{#dto.terminateTime}      IS NULL OR terminate_time      = :#{#dto.terminateTime}) " +
                    "  AND (:#{#dto.terminateType}      IS NULL OR terminate_type      = :#{#dto.terminateType}) " +
                    "  AND (:#{#dto.signMode}           IS NULL OR sign_mode           = :#{#dto.signMode}) " +
                    "  AND (:#{#dto.dataProductId}      IS NULL OR data_product_id     = :#{#dto.dataProductId}) " +
                    "  AND (:#{#dto.strategyId}         IS NULL OR strategy_id         = :#{#dto.strategyId}) " +
                    "  AND (:#{#dto.signatoryTime}      IS NULL OR signatory_time      = :#{#dto.signatoryTime}) " +
                    "  AND (:#{#dto.contractStatus}     IS NULL OR contract_status     = :#{#dto.contractStatus}) " +
                    "  AND (:#{#dto.providerSignature}  IS NULL OR provider_signature  = :#{#dto.providerSignature}) " +
                    "  AND (:#{#dto.providerSignStatus} IS NULL OR provider_sign_status = :#{#dto.providerSignStatus}) " +
                    "  AND (:#{#dto.consumerSignStatus} IS NULL OR consumer_sign_status = :#{#dto.consumerSignStatus}) " +
                    "  AND (:#{#dto.contractHash}       IS NULL OR contract_hash       = :#{#dto.contractHash}) " +
                    "  AND (:#{#dto.expansionItem}      IS NULL OR expansion_item      LIKE CONCAT('%',:#{#dto.expansionItem},'%')) " +
                    "  AND (:#{#dto.platformId}         IS NULL OR platform_id         = :#{#dto.platformId}) " +
                    "  AND (:#{#dto.contractVersion}    IS NULL OR contract_version    = :#{#dto.contractVersion}) " +
                    "  AND (:#{#dto.providerUserId}     IS NULL OR provider_user_id    = :#{#dto.providerUserId}) " +
                    "  AND (:#{#dto.consumerUserId}     IS NULL OR consumer_user_id    = :#{#dto.consumerUserId}) " +
                    "  AND (:#{#dto.currentHandlerRole} IS NULL OR current_handler_role = :#{#dto.currentHandlerRole}) " +
                    "ORDER BY issue_time DESC",
            nativeQuery = true)
    List<DigitalContractDO> select(@Param("dto") DigitalContractDO dto);

    /* ================== 删除 ================== */

    // 物理删除
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM digital_contract WHERE contract_id = :contractId", nativeQuery = true)
    int hardDelete(@Param("contractId") String contractId);

}