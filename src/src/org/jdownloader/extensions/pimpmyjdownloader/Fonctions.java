package org.jdownloader.extensions.pimpmyjdownloader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fonctions {



	public static String getDateFormat(Date date, String format_Ex_YYYY_MM_DD) {
		if (format_Ex_YYYY_MM_DD == null) {
			format_Ex_YYYY_MM_DD = "yyyyMMddHHmmss";
		}
		DateFormat dateFormat = new SimpleDateFormat(format_Ex_YYYY_MM_DD);
		return dateFormat.format(date);
	}

	public static void patience(Long millisecond) {
		try {
			Thread.currentThread().wait(millisecond);
		} catch (Exception e) {
			// Try with a Thread.currentThread().sleep(new Long(1000 * 300));
			try {
				Thread.sleep(new Long(millisecond));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static String getFieldFromString(String chaine, String delimiteur, Integer Field) {
		String[] liste = chaine.split(delimiteur);
		String retour = "";
		try {
			retour = liste[Field];
		} catch (Exception e) {

		}
		return retour;
	}

	public static Date getDateFormat(String date, String format_Ex_YYYY_MM_DD) {
		Date dt = null;
		try {
			if (format_Ex_YYYY_MM_DD == null) {
				format_Ex_YYYY_MM_DD = "yyyyMMddHHmmss";
			}
			SimpleDateFormat df = new SimpleDateFormat(format_Ex_YYYY_MM_DD);

			dt = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dt;

	}

}
