package com.czechowski.webapp.controller;

import com.czechowski.service.DateAndTimeServices;
import com.czechowski.service.ReservationComparator;
import com.czechowski.webapp.domain.Reservation;
import com.czechowski.webapp.repositories.ReservationRepositorie;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReservationController {

    private List<Reservation> offerReservationList=new ArrayList<>();

    private ReservationRepositorie repositorie;

    @Autowired
    public ReservationController(ReservationRepositorie repositorie) {
        this.repositorie = repositorie;
    }

    @RequestMapping(value = "/reservation/list", method = RequestMethod.POST)
    public String add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        offerReservationList.clear();
        String timeInString = request.getParameter("timeOfMeeting");
        Long timeOfMeeting = Long.valueOf(timeInString);
        String dateInString = request.getParameter("timeanddate");
        Date start = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        try {
            start = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Reservation reservation = new Reservation();
        reservation.setTime(timeOfMeeting);
        reservation.setStart(start);
        boolean flag = isEmptyTimeToReservation(reservation);
        if (flag) {
            repositorie.save(reservation);
        }else{
            createOfferReservationList(reservation);
        }
        return "redirect:/reservation/list";
    }

    @RequestMapping(value = "/reservation/list", method = RequestMethod.GET)
    public String listReservation(Model model) {
        List<Reservation> list = reservationsToList();
        model.addAttribute("reservations", list);
        model.addAttribute("offer",offerReservationList);
        for (int i = 0; i < offerReservationList.size(); i++) {
            System.out.println(offerReservationList.get(i));
        }
        return "reservation/list";
    }


    @RequestMapping(value = "/remove/", params = "id")
    public String removeReservation(@RequestParam("id") String id) {
        Long l = Long.valueOf(id);
        repositorie.delete(l);
        return "redirect:/reservation/list";
    }

    @RequestMapping(value = "/addOffer/", params = { "time", "start" })
    public String addOffer(@RequestParam("time") String time,@RequestParam("start") String start) {
        offerReservationList.clear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date=null;
        try {
             date = simpleDateFormat.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long meetingTime = Long.valueOf(time);
       Reservation reservation=new Reservation(date,meetingTime);
       repositorie.save(reservation);
        return "redirect:/reservation/list";
    }



    private List<Reservation> reservationsToList() {
        List<Reservation> list = new ArrayList<>();
        repositorie.findAll().forEach(e -> list.add(e));
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        Collections.sort(list, new ReservationComparator());
        return list;
    }

    private boolean isEmptyTimeToReservation(Reservation reservation) {
        boolean flag = false;
        DateAndTimeServices dateAndTimeServices = new DateAndTimeServices();
        Date end = dateAndTimeServices.datePlusMinutes(reservation.getStart(), reservation.getTime());
        Date start = reservation.getStart();
        List<Reservation> list = reservationsToList();

        if (end.before(list.get(0).getStart())) {
            flag = true;
        }
        if (flag == false) {
            Date tmpStart = list.get(list.size() - 1).getStart();
            Long tmpTime = list.get(list.size() - 1).getTime();
            if (start.after(dateAndTimeServices.datePlusMinutes(tmpStart, tmpTime))) {
                flag = true;
            }
        }
        if (flag == false) {
            for (int i = 1; i < list.size(); i++) {
                if (start.before(list.get(i).getStart())
                        && end.before(list.get(i).getStart())
                        && start.after(dateAndTimeServices.datePlusMinutes(list.get(i - 1).getStart(), list.get(i - 1).getTime()))) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private void createOfferReservationList(Reservation reservation){
        offerReservationList.clear();
        DateAndTimeServices dateAndTimeServices=new DateAndTimeServices();
        List<Reservation> list = reservationsToList();
        Reservation tmpReservation=new Reservation();

        Date startDateFromList=list.get(0).getStart();
        Long tmpLong=reservation.getTime();
        Date tmpStart=dateAndTimeServices.dateMinusMinutes(startDateFromList,(tmpLong+5L));
        tmpReservation.setStart(tmpStart);
        tmpReservation.setTime(tmpLong);
        offerReservationList.add(tmpReservation);

        for (int i = 0; i < list.size()-1; i++) {
            Reservation tmpMidReservation=new Reservation();
            startDateFromList=list.get(0).getStart();
            Date endTimeFromList=dateAndTimeServices.datePlusMinutes(list.get(i).getStart(),list.get(i).getTime());
            if (dateAndTimeServices.datePlusMinutes(endTimeFromList, 60L).before(list.get(i + 1).getStart())) {
                tmpStart=dateAndTimeServices.datePlusMinutes(startDateFromList,5L);
                tmpMidReservation.setStart(tmpStart);
                tmpMidReservation.setTime(50L);
                offerReservationList.add(tmpMidReservation);
            }
        }
        Reservation tmpLastReservation=new Reservation();
        tmpStart=dateAndTimeServices.datePlusMinutes(list.get(list.size()-1).getStart(),5L);
        tmpLastReservation.setStart(tmpStart);
        tmpLastReservation.setTime(reservation.getTime());
        offerReservationList.add(tmpLastReservation);
    }
}
