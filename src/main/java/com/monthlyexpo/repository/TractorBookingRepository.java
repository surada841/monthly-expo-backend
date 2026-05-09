package com.monthlyexpo.repository;

import com.monthlyexpo.entity.TractorBooking;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TractorBookingRepository
        extends JpaRepository<TractorBooking, Long> {

    @Query("""
        select b from TractorBooking b
        where (:village is null
        or lower(b.customer.villageName)
        like lower(concat('%',:village,'%')))
        and (:from is null or b.bookingDate >= :from)
        and (:to is null or b.bookingDate <= :to)
    """)
    List<TractorBooking> filter(
            @Param("village") String village,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
        select coalesce(sum(b.amountPaid),0)
        from TractorBooking b
        where b.bookingDate between :from and :to
    """)
    BigDecimal paidIncomeBetween(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    // ADD THIS METHOD
    List<TractorBooking> findByCustomerId(Long customerId);
}