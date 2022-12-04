package com.MovieTown.utils;

import com.MovieTown.beans.Screening;
import com.MovieTown.beans.Seat;
import com.MovieTown.repositories.ScreeningRepository;
import com.MovieTown.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScreeningDeleteJob implements Runnable{

    private ScreeningRepository screeningRepository;
    private SeatRepository seatRepository;
    private boolean quit;

    public ScreeningDeleteJob(ScreeningRepository screeningRepository, SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }

    public void stop() {
        quit = true;
    }

    /***
     * This method runs every hour and deletes the screenings whose time of screening has passed
     */
    @Override
    public void run() {
        while(! quit){
            List<Screening> screenings = screeningRepository.findByTimeLessThan(new Date(System.currentTimeMillis()));
            screenings.forEach(screening -> screeningRepository.delete(screening));
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
