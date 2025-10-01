package com.idea101.backendengine.restaurant.repository;

import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.entity.OutletHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutletHoursRepository extends JpaRepository<OutletHours, Long> {

    List<OutletHours> findByOutletAndDayOfWeek(Outlet outlet, DayOfWeek dayOfWeek);

    List<OutletHours> findByOutletAndSpecialDate(Outlet outlet, LocalDate specialDate);

    List<OutletHours> findByOutlet(Outlet outlet);
}
