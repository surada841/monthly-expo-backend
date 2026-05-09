package com.monthlyexpo.service;

import com.monthlyexpo.dto.request.CustomerBulkPaymentRequest;
import com.monthlyexpo.dto.response.CustomerBookingSummaryResponse;
import com.monthlyexpo.entity.TractorBooking;

public interface BookingService {
	TractorBooking completeDelivery(Long id);
	CustomerBookingSummaryResponse getCustomerBookings(Long customerId);

	CustomerBookingSummaryResponse payCustomerBookings(
	        Long customerId,
	        CustomerBulkPaymentRequest request
	);

}
