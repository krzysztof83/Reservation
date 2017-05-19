package com.czechowski.service;

import com.czechowski.webapp.domain.Reservation;
import java.util.Comparator;

public class ReservationComparator implements Comparator<Reservation> {
    @Override
    public int compare(Reservation r1, Reservation r2) {
        return r1.getStart().compareTo(r2.getStart());
    }
}
