package com.feicuiedu.atm.view;

import com.feicuiedu.atm.view.handler.ViewTarget;

public class PhaseProcess implements Runnable {
    
    ViewTarget target;
    Integer phase;

    public PhaseProcess(ViewTarget target, Integer phase) {
        this.target = target;
        this.phase = phase;
    }
    
    @Override
    public void run() {
        target.setParameter("phase", phase);
    }
    
}
