package com.MovieTown.beans;

import com.MovieTown.services.Registered;

public class MySession {

    private Registered service;
    private long lastActive;

    public MySession(Registered service, long lastActive) {
        this.service = service;
        this.lastActive = lastActive;
    }

    public Registered getService() {
        return service;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }

    @Override
    public String toString() {
        return "MySession{" +
                "service=" + service.toString() +
                '}';
    }
}
