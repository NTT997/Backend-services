package com.salesmanager.core.business.repositories.policy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.policy.Policy;
import com.salesmanager.core.model.user.User;

import java.util.Date;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    // Use default JpaRepository findAll
    List<Policy> findAll();

    // Use default JpaRepository findAll with pagination
    Page<Policy> findAll(Pageable pageable);

    @Query("SELECT p FROM Policy p JOIN FETCH p.createdBy u WHERE p.active = :active")
    List<Policy> findByActive(@Param("active") boolean active);

    @Query("SELECT p FROM Policy p JOIN FETCH p.createdBy u WHERE p.createdBy = :createdBy")
    List<Policy> findByCreatedBy(@Param("createdBy") User createdBy);

    @Query("SELECT p FROM Policy p WHERE LOWER(p.title) = LOWER(:title)")
    List<Policy> findByTitle(@Param("title") String title);

    @Query("SELECT p FROM Policy p WHERE p.type = :type")
    List<Policy> findByType(@Param("type") String type);

    @Query("SELECT p FROM Policy p WHERE LOWER(p.descriptions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Policy> findByDescriptionsContaining(@Param("keyword") String keyword);

    @Query("SELECT p FROM Policy p WHERE p.lastUpdate BETWEEN :startDate AND :endDate")
    List<Policy> findByLastUpdateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT p FROM Policy p WHERE p.auditSection.dateCreated BETWEEN :startDate AND :endDate")
    List<Policy> findByCreatedDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT p FROM Policy p WHERE p.auditSection.dateModified BETWEEN :startDate AND :endDate")
    List<Policy> findByModifiedDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT p FROM Policy p WHERE p.auditSection.modifiedBy = :modifiedBy")
    List<Policy> findByModifiedBy(@Param("modifiedBy") String modifiedBy);

    @Query("SELECT p FROM Policy p WHERE p.active = :active AND p.type = :type")
    List<Policy> findByActiveAndType(@Param("active") boolean active, @Param("type") String type);

    @Query("SELECT p FROM Policy p JOIN FETCH p.createdBy u WHERE p.createdBy = :createdBy AND p.auditSection.dateCreated BETWEEN :startDate AND :endDate")
    List<Policy> findByCreatedByAndCreatedDateBetween(@Param("createdBy") User createdBy, 
                                                     @Param("startDate") Date startDate, 
                                                     @Param("endDate") Date endDate);
}