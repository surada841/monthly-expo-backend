package com.monthlyexpo.repository;

import com.monthlyexpo.entity.TractorBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<TractorBooking, Long> {
}