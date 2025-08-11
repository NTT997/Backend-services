package com.salesmanager.core.model.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;

/**
 * Global system configuration information
 * @author casams1
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SYSTEM_CONFIGURATION")
public class SystemConfiguration extends SalesManagerEntity<Long, SystemConfiguration> implements Serializable, Auditable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SYSTEM_CONFIG_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SYST_CONF_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="CONFIG_KEY")
	private String key;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name = "TOTAL_APPROVERS")
	private int totalApprovers;
	
	private Long min;
	private Long max;
	
	@OneToMany(mappedBy = "systemConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SystemConfigApprover> approvers;

	public List<SystemConfigApprover> getApprovers() {
	    return approvers;
	}

	public void setApprovers(List<SystemConfigApprover> approvers) {
	    this.approvers = approvers;
	}

	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return name;
	}

	public void setValue(String name) {
		this.name = name;
	}

	public int getTotalApprovers() {
		return totalApprovers;
	}

	public void setTotalApprovers(int totalApprovers) {
		this.totalApprovers = totalApprovers;
	}

	public Long getMin() {
		return min;
	}

	public void setMin(Long min) {
		this.min = min;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}
	
}
