package edu.gmu.srct.barbican.server.trends;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mgauto on 10/25/14.
 */
public class TrendingEngine implements Runnable{
    private boolean running = true;
    private ArrayList<Trend> trends = new ArrayList<Trend>();
    private ReentrantLock trendWriteLock = new ReentrantLock();
    @Override
    public void run() {
        while(trendWriteLock.isLocked());
        for(Trend t : trends) {
            t.check();
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registerTrend(Trend trend) {
        while(!trendWriteLock.tryLock());
        trends.add(trend);
        trendWriteLock.unlock();
    }

    public ArrayList<Trend> getTrends() {
        return trends;
    }
}
