package com.chrisdufort.timer;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.JAGEmailClient.controllers.RootLayoutController;

/**
 * This class is 
 * @author Christopher
 * @version 0.4.5-SNAPSHOT - phase 4, last modified 12/13/2015
 * @since 0.4.5
 */
public class RefreshTimer {

    private final Timer timer;

    public RefreshTimer(RootLayoutController rootController) {

        LocalDateTime ldt = LocalDateTime.now().plusMinutes(1);
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date alarmDateTime = Date.from(instant);

        System.out.println("Time at which next alarm will sound " + alarmDateTime);
        timer = new Timer();
        //timer.schedule(new RefreshTask(rootController), alarmDateTime);
        timer.scheduleAtFixedRate(new RefreshTask(rootController), alarmDateTime, 300000);
    }

}

/**
 * This class is an extention of the TimerTask and will be called every so often to refresh the database of emails.
 * @author Christopher
 * @version 0.4.5-SNAPSHOT - phase 4, last modified 12/13/2015
 * @since 0.4.5
 */
class RefreshTask extends TimerTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private RootLayoutController rootController;

	public RefreshTask(RootLayoutController rootController) {
		this.rootController = rootController;
	}

	@Override
    public void run() {
		log.debug("Refreshing emails on timer");
    	try {
			this.rootController.refreshEmails();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
