package com.MovieTown.utils;

import com.MovieTown.beans.MySession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionsJob implements Runnable{

    private HashMap<Integer, MySession> sessions;
    long limit = 1000*60*30; //You need to remove the session after 30 minutes

    private boolean quit;

    public SessionsJob(HashMap<Integer, MySession> sessions) {
        this.sessions = sessions;
    }

    public void stop() {
        quit = true;
    }

    /***
     * This method runs with the server.
     * It checks the sessions map for expired sessions and deletes them.
     */
    @Override
    public void run() {
        try{
            while(! quit){
                List<Integer> keysExpired = new ArrayList<>();
                for(Map.Entry<Integer, MySession> session : sessions.entrySet()){
                    if(System.currentTimeMillis() - session.getValue().getLastActive() > limit){
                        keysExpired.add(session.getKey());
                    }
                }
                for(Integer expiredKey: keysExpired){
                    sessions.remove(expiredKey);
                }
                Thread.sleep(1000*60*2);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
