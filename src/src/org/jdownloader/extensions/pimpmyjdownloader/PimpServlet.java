package org.jdownloader.extensions.pimpmyjdownloader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class PimpServlet extends HttpServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		String link = request.getParameter("link");
		String force= request.getParameter("force");
		String state = request.getParameter("state");
		String clear = request.getParameter("clear");
		String info = request.getParameter("info");
		String logs = request.getParameter("logs");
		String version = request.getParameter("version");
		if (link != null && !"".equals(link) && link.startsWith("http")) {
			PimpMyJDownloaderExtension.log("Collecting a link " + link);
			PimpMyJDownloaderExtension.getInstance().addLink(link,force);
			PimpMyJDownloaderExtension.log(this + " Link " + link + " has been added to queue");
		}
		if ( state != null && !"".equals(state))
		{
			Gson aGson=new Gson();
			String states=aGson.toJson(PimpMyJDownloaderExtension.getInstance().getMonkeysDownload().values());
			response.getOutputStream().println(states);
			response.flushBuffer();
		}
		
		if ( clear != null && !"".equals(clear))
		{
			PimpMyJDownloaderExtension.getInstance().removeLinkByLink(clear);
		}
		
		if ( info != null && !"".equals(info))
		{
			Gson aGson=new Gson();
			String infos=aGson.toJson(PimpMyJDownloaderExtension.getInstance().getMonkeyDowloadByLink(info));
			response.getOutputStream().println(infos);
			response.flushBuffer();
		}
		if (logs != null && !"".equals(logs)) {
	            Gson aGson = new Gson();
	            String infos = aGson.toJson(PimpMyJDownloaderExtension.getLogs());
	            response.getOutputStream().println(infos);
	            response.flushBuffer();
	        }
		
		if (version != null && !"".equals(version)) {
            response.getOutputStream().println("1.0");
            response.flushBuffer();
        }
	}

}
