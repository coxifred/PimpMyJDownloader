package org.jdownloader.extensions.pimpmyjdownloader;

import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

public class JettyLogger extends AbstractLogger{
	
	public static boolean debug=false;

	@Override
	public String getName() {
		return "JettyLogger";
	}

	@Override
	public void warn(String msg, Object... args) {
		PimpMyJDownloaderExtension.log(format(msg, args));
		
	}

	@Override
	public void warn(Throwable thrown) {
		PimpMyJDownloaderExtension.log(thrown.getMessage());
		
	}

	@Override
	public void warn(String msg, Throwable thrown) {
		PimpMyJDownloaderExtension.log(msg + " " + thrown.getMessage());
		
	}

	@Override
	public void info(String msg, Object... args) {
		PimpMyJDownloaderExtension.log(format(msg, args));
		
	}

	@Override
	public void info(Throwable thrown) {
		PimpMyJDownloaderExtension.log(thrown.getMessage());
		
	}

	@Override
	public void info(String msg, Throwable thrown) {
		PimpMyJDownloaderExtension.log(msg + " " + thrown.getMessage());
		
	}

	@Override
	public boolean isDebugEnabled() {
		return debug;
	}

	@Override
	public void setDebugEnabled(boolean enabled) {

		
	}

	@Override
	public void debug(String msg, Object... args) {
		PimpMyJDownloaderExtension.log(format(msg, args));
		
	}

	@Override
	public void debug(Throwable thrown) {

		PimpMyJDownloaderExtension.log(thrown.getMessage());
		
	}

	@Override
	public void debug(String msg, Throwable thrown) {
		
		PimpMyJDownloaderExtension.log(msg + " " + thrown.getMessage());
		
	}

	@Override
	public void ignore(Throwable ignored) {

		PimpMyJDownloaderExtension.log(ignored.getMessage());
		
	}

	@Override
	protected Logger newLogger(String fullname) {
		return new JettyLogger();
	}

	
	private String format(String msg, Object... args)
    {
        msg = String.valueOf(msg); // Avoids NPE
        String braces = "{}";
        StringBuilder builder = new StringBuilder();
        int start = 0;
        for (Object arg : args)
        {
            int bracesIndex = msg.indexOf(braces, start);
            if (bracesIndex < 0)
            {
                builder.append(msg.substring(start));
                builder.append(" ");
                builder.append(arg);
                start = msg.length();
            }
            else
            {
                builder.append(msg.substring(start, bracesIndex));
                builder.append(String.valueOf(arg));
                start = bracesIndex + braces.length();
            }
        }
        builder.append(msg.substring(start));
        return builder.toString();
    }
}
