package com.czechowski.webapp.bootstrap;

import com.czechowski.service.DateAndTimeServices;
import com.czechowski.webapp.domain.Reservation;
import com.czechowski.webapp.repositories.ReservationRepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ReservationRepositorie repositorie;

    @Autowired
    public Bootstrap(ReservationRepositorie repositorie) {
        this.repositorie = repositorie;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        DateAndTimeServices dateAndTimeServices=new DateAndTimeServices();

        Reservation reservation= new Reservation();
        reservation.setStart(new Date());
        reservation.setTime(50L);

        repositorie.save(reservation);

        Reservation reservation2= new Reservation();
        reservation2.setStart(dateAndTimeServices.datePlusMinutes(new Date(),80L));
        reservation2.setTime(110L);

        repositorie.save(reservation2);

        Reservation reservation3= new Reservation();
        Date d=new Date();
        reservation3.setStart(dateAndTimeServices.datePlusMinutes(new Date(),300L));
        reservation3.setTime(70L);

        repositorie.save(reservation3);
    }
}
