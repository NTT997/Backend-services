package com.salesmanager.shop.model.system;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class ParameterConfiguration {
	public Long SystemConfigId;
	
	public String ConfigKey;

    public String ConfigValue;

    ParameterConfiguration(){}

    ParameterConfiguration(Long SystemConfigId,String ConfigKey,String ConfigValue){
        this.SystemConfigId = SystemConfigId;
        this.ConfigKey = ConfigKey;
        this.ConfigValue = ConfigValue;
    }

    public Long getSystemConfigId() {
        return SystemConfigId;
    }

    public void setSystemConfigId(Long systemConfigId) {
        SystemConfigId = systemConfigId;
    }

    public String getConfigKey() {
        return ConfigKey;
    }

    public void setConfigKey(String configKey) {
        ConfigKey = configKey;
    }

    public String getConfigValue() {
        return ConfigValue;
    }

    public void setConfigValue(String configValue) {
        ConfigValue = configValue;
    }

    @Override
    public String toString() {
        return "ParameterConfiguration [SystemConfigId=" + SystemConfigId + ", ConfigKey=" + ConfigKey
                + ", ConfigValue=" + ConfigValue + "]";
    }

    
    

}
