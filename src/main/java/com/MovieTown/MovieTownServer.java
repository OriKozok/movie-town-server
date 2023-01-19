package com.MovieTown;

import com.MovieTown.utils.ScreeningDeleteJob;
import com.MovieTown.utils.SessionsJob;
import org.springframework.stereotype.Service;

@Service
public class MovieTownServer {

    private ScreeningDeleteJob screeningDeleteJob;

    private SessionsJob sessionsJob;

    public MovieTownServer(ScreeningDeleteJob screeningDeleteJob, SessionsJob sessionsJob) {
        this.screeningDeleteJob = screeningDeleteJob;
        this.sessionsJob = sessionsJob;
    }

    public void runApplication() {
        Thread thread = new Thread(screeningDeleteJob);
        Thread thread2 = new Thread(sessionsJob);
        try {
            thread.start();
            thread2.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            screeningDeleteJob.stop();
            sessionsJob.stop();
        }
    }
}
