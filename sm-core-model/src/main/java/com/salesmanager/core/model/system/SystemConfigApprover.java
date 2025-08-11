package com.salesmanager.core.model.system;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SYSTEM_CONFIG_APPROVERS")
public class SystemConfigApprover extends SalesManagerEntity<Long, SystemConfigApprover> implements Auditable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "APPROVER_EMAIL")
	private String approverEmail;
	
	@Column(name = "\"ORDER\"")
	private int order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_config_id", nullable = false)
	@JsonIgnore
	private SystemConfiguration systemConfig;

	public SystemConfiguration getSystemConfig() {
		return systemConfig;
	}

	public void setSystemConfig(SystemConfiguration systemConfig) {
		this.systemConfig = systemConfig;
	}
	
	public String getApproverEmail() {
		return approverEmail;
	}

	public void setApproverEmail(String approverEmail) {
		this.approverEmail = approverEmail;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Embedded
	private AuditSection audit = new AuditSection();

	@Override
	public AuditSection getAuditSection() {
		// TODO Auto-generated method stub
		return audit;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

}
