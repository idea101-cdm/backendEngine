package com.idea101.backendengine.restaurant.service;

import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.entity.OutletHours;
import com.idea101.backendengine.restaurant.repository.OutletHoursRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class OutletHoursService {

    private final OutletHoursRepository outletHoursRepository;

    public OutletHoursService(OutletHoursRepository outletHoursRepository) {
        this.outletHoursRepository = outletHoursRepository;
    }

    @Transactional
    public OutletHours addShift(Outlet outlet, DayOfWeek dayOfWeek,
                                LocalDate specialDate,
                                String openTime, String closeTime) {

        List<OutletHours> existingShifts;
        if (specialDate != null) {
            existingShifts = outletHoursRepository.findByOutletAndSpecialDate(outlet, specialDate);
        } else {
            existingShifts = outletHoursRepository.findByOutletAndDayOfWeek(outlet, dayOfWeek);
        }

        int nextShiftNumber = existingShifts.stream()
                .mapToInt(OutletHours::getShiftNumber)
                .max()
                .orElse(0) + 1;


        OutletHours newShift = new OutletHours();
        newShift.setOutlet(outlet);
        newShift.setDayOfWeek(dayOfWeek);
        newShift.setSpecialDate(specialDate);
        newShift.setShiftNumber(nextShiftNumber);
        newShift.setOpenTime(java.time.LocalTime.parse(openTime));
        newShift.setCloseTime(java.time.LocalTime.parse(closeTime));

        return outletHoursRepository.save(newShift);
    }

    @Transactional
    public void addDefaultWeeklySchedule(Outlet outlet, String defaultOpenTime, String defaultCloseTime) {
        for (DayOfWeek day : DayOfWeek.values()) {
            addShift(outlet, day, null, defaultOpenTime, defaultCloseTime);
        }
    }

}
