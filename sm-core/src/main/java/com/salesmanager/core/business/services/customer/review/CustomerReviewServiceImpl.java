package com.salesmanager.core.business.services.customer.review;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.customer.review.CustomerReviewRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.review.CustomerReview;

@Service("customerReviewService")
public class CustomerReviewServiceImpl extends SalesManagerEntityServiceImpl<Long, CustomerReview>
		implements CustomerReviewService {

	private CustomerReviewRepository customerReviewRepository;

	@Inject
	private CustomerService customerService;

	@Inject
	public CustomerReviewServiceImpl(CustomerReviewRepository customerReviewRepository) {
		super(customerReviewRepository);
		this.customerReviewRepository = customerReviewRepository;
	}

//	private void saveOrUpdate(CustomerReview review) throws ServiceException {
//		
//
//		Validate.notNull(review,"CustomerReview cannot be null");
//		Validate.notNull(review.getCustomer(),"CustomerReview.customer cannot be null");
//		Validate.notNull(review.getReviewedCustomer(),"CustomerReview.reviewedCustomer cannot be null");
//		
//		
//		//refresh customer
//		Customer customer = customerService.getById(review.getReviewedCustomer().getId());
//		
//		//ajust product rating
//		Integer count = 0;
//		if(customer.getCustomerReviewCount()!=null) {
//			count = customer.getCustomerReviewCount();
//		}
//				
//		
//		
//
//		BigDecimal averageRating = customer.getCustomerReviewAvg();
//		if(averageRating==null) {
//			averageRating = new BigDecimal(0);
//		}
//		//get reviews
//
//		
//		BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
//		totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));
//		
//		count = count + 1;
//		double avg = totalRating.doubleValue() / count;
//		
//		customer.setCustomerReviewAvg(new BigDecimal(avg));
//		customer.setCustomerReviewCount(count);
//		super.save(review);
//		
//		customerService.update(customer);
//		
//		review.setReviewedCustomer(customer);
//
//		
//	}

	private void saveOrUpdate(CustomerReview review) throws ServiceException {
		Validate.notNull(review, "CustomerReview cannot be null");
		Validate.notNull(review.getCustomer(), "CustomerReview.customer cannot be null");
		Validate.notNull(review.getReviewedCustomer(), "CustomerReview.reviewedCustomer cannot be null");

		// Refresh customer
		Customer customer = customerService.getById(review.getReviewedCustomer().getId());
		if (review.getId() != null && review.getId() > 0) {
			// Updating an existing review
			CustomerReview existingReview = this.getById(review.getId());

			if (existingReview == null) {
				throw new ServiceException("Existing review not found for update", new Throwable().getCause());
			}

			// Update review in DB
			super.update(review);
		} else {
			// Check if already reviewed
			CustomerReview existingReview = this.getByReviewerAndReviewed(review.getCustomer().getId(),
					review.getReviewedCustomer().getId());

			if (existingReview != null) {
				throw new ServiceException("This customer has already reviewed the target customer.",
						new Throwable().getCause());
			}

			// Save new review
			super.create(review);
		}

		// Recalculate average and count from all persisted reviews
		List<CustomerReview> allReviews = this.getByReviewedCustomer(customer);

		double totalRating = 0.0;
		for (CustomerReview r : allReviews) {
			totalRating += r.getReviewRating();
		}

		int reviewCount = allReviews.size();
		double avgRating = reviewCount > 0 ? totalRating / reviewCount : 0.0;

		customer.setCustomerReviewAvg(BigDecimal.valueOf(avgRating));
		customer.setCustomerReviewCount(reviewCount);

		// Update customer with accurate stats
		customerService.update(customer);

		// Assign updated customer back
		review.setReviewedCustomer(customer);
		
	}

	public void update(CustomerReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}

	public void create(CustomerReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}

	@Override
	public List<CustomerReview> getByCustomer(Customer customer) {
		Validate.notNull(customer, "Customer cannot be null");
		return customerReviewRepository.findByReviewer(customer.getId());
	}

	@Override
	public List<CustomerReview> getByReviewedCustomer(Customer customer) {
		Validate.notNull(customer, "Customer cannot be null");
		return customerReviewRepository.findByReviewed(customer.getId());
	}

	@Override
	public CustomerReview getByReviewerAndReviewed(Long reviewer, Long reviewed) {
		Validate.notNull(reviewer, "Reviewer customer cannot be null");
		Validate.notNull(reviewed, "Reviewed customer cannot be null");
		return customerReviewRepository.findByRevieweAndReviewed(reviewer, reviewed);
	}

}
