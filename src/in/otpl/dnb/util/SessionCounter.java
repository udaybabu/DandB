package in.otpl.dnb.util;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

public class SessionCounter implements HttpSessionListener {

	private static int activeSessions = 0;

	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		if(activeSessions > 0)
			activeSessions--;
	}

	public static int getActiveSessions() {
		return activeSessions;
	}
}
