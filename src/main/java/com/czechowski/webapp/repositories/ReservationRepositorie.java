package com.czechowski.webapp.repositories;

import com.czechowski.webapp.domain.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReservationRepositorie extends CrudRepository<Reservation,Long> {
}
