package com.salesmanager.shop.mapper.tax;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;
import com.salesmanager.core.model.tax.taxrate.TaxRate;
import com.salesmanager.core.model.tax.taxrate.TaxRateDescription;
import com.salesmanager.shop.mapper.Mapper;
import com.salesmanager.shop.model.tax.ReadableTaxClass;
import com.salesmanager.shop.model.tax.ReadableTaxRate;
import com.salesmanager.shop.model.tax.ReadableTaxRateDescription;

@Component
public class ReadableTaxRateMapper implements Mapper<TaxRate, ReadableTaxRate> {

	@Override
	public ReadableTaxRate convert(TaxRate source, MerchantStore store, Language language) {
		ReadableTaxRate taxRate = new ReadableTaxRate();
		return this.merge(source, taxRate, store, language);

	}

//	@Override
//	public ReadableTaxRate merge(TaxRate source, ReadableTaxRate destination, MerchantStore store,
//								 Language language) {
//		Validate.notNull(destination, "destination TaxRate cannot be null");
//		Validate.notNull(source, "source TaxRate cannot be null");
//		destination.setId(source.getId());
//		destination.setCountry(source.getCountry().getIsoCode());
//		destination.setZone(source.getZone().getCode());
//		destination.setRate(source.getTaxRate().toString());
//		destination.setCode(source.getCode());
//		destination.setPriority(source.getTaxPriority());
//		Optional<ReadableTaxRateDescription> description = this.convertDescription(source.getDescriptions(), language);
//		if(description.isPresent()) {
//			destination.setDescription(description.get());
//		}
//		return destination;
//	}

	@Override
	public ReadableTaxRate merge(TaxRate source, ReadableTaxRate destination, MerchantStore store, Language language) {
		Validate.notNull(destination, "destination TaxRate cannot be null");
		Validate.notNull(source, "source TaxRate cannot be null");
		destination.setId(source.getId());
		destination.setCountry(source.getCountry().getIsoCode());
		destination.setZone(source.getZone().getCode());
		destination.setRate(source.getTaxRate().toString());
		destination.setCode(source.getCode());
		destination.setPriority(source.getTaxPriority());

		Optional<ReadableTaxRateDescription> description = this.convertDescription(source.getDescriptions(), language);
		description.ifPresent(destination::setDescription);

		if (source.getTaxClass() != null) {
			TaxClass sourceTaxClass = source.getTaxClass();
			ReadableTaxClass readableTaxClass = new ReadableTaxClass();
			readableTaxClass.setId(sourceTaxClass.getId());
			readableTaxClass.setCode(sourceTaxClass.getCode());
			readableTaxClass.setName(sourceTaxClass.getTitle());
			readableTaxClass.setStore(store.getCode()); // Optional: or sourceTaxClass.getStore() if available
			destination.setTaxClass(readableTaxClass);
		}

		return destination;
	}

	private Optional<ReadableTaxRateDescription> convertDescription(List<TaxRateDescription> descriptions,
			Language language) {
		Validate.notEmpty(descriptions, "List of TaxRateDescriptions should not be empty");

		Optional<TaxRateDescription> description = descriptions.stream()
				.filter(desc -> desc.getLanguage().getCode().equals(language.getCode())).findAny();
		if (description.isPresent()) {
			return Optional.of(convertDescription(description.get()));
		} else {
			return Optional.empty();
		}

	}

	private ReadableTaxRateDescription convertDescription(TaxRateDescription desc) {
		ReadableTaxRateDescription d = new ReadableTaxRateDescription();
		d.setDescription(desc.getDescription());
		d.setName(desc.getName());
		d.setLanguage(desc.getLanguage().getCode());
		d.setDescription(desc.getDescription());
		d.setId(desc.getId());
		d.setTitle(desc.getTitle());
		return d;
	}

}
