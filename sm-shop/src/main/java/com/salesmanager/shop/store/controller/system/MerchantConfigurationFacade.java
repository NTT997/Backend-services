package com.salesmanager.shop.store.controller.system;

import java.util.List;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.system.Configs;
import com.salesmanager.shop.model.system.PersistableIntegrationConfiguration;
import com.salesmanager.shop.model.system.ReadableMerchantConfiguration;

public interface MerchantConfigurationFacade {

  Configs getMerchantConfig(MerchantStore merchantStore, Language language);
  
  //huy-----------
  void saveConfiguratation(List<PersistableIntegrationConfiguration> configs ,MerchantStore merchantStore, Language language);
  
  ReadableMerchantConfiguration getListPaymentConfig(MerchantStore merchantStore, Language language);
  
  //--------------
}
