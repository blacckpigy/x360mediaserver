package net.sourceforge.x360mediaserve.utils;

public class StringUtils {
	public static String getHtmlString(String inputString){
		String result=inputString.replace("&","&amp;");
		result.replace("�","&ouml;");
		result.replace("�","&iuml;");
		
		return result;
	}
}