package x360mediaserver.utils;

public class StringUtils
{
    public static String getHtmlString(String inputString)
    {
        String result = inputString.replaceAll("&", "&amp;");
        result.replaceAll("ö", "&ouml;");
        result.replaceAll("ï", "&iuml;");
        result.replaceAll("\\", "&apos;");
        result.replaceAll("<", "&lt;");
        result.replaceAll(">", "&gt;");
        result.replaceAll("\n", "&#xA;");
        result.replaceAll("\r", "&#xD;");
        result.replaceAll("\t", "&#x9;");
        result.replaceAll("\"", "&quot;");

        return result;
    }

    public static String getXMLString(String inputString)
    {
        String result = inputString.replaceAll("&amp;", "&");
        result.replaceAll("&ouml;", "ö");
        result.replaceAll("&iuml;", "ï");
        result.replaceAll("&apos;", "\\");
        result.replaceAll("&lt;", "<");
        result.replaceAll("&gt;", ">");
        result.replaceAll("&#xA;", "\n");
        result.replaceAll("&#xD;", "\r");
        result.replaceAll("&#x9;", "\t");
        result.replaceAll("&quot;", "\"");

        return result;
    }
}