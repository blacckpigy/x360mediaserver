package x360mediaserver.utils;

public class StringUtils
{
    public static String getHtmlString(String inputString)
    {
        String result = inputString.replace("&", "&amp;");
        result.replace("ö", "&ouml;");
        result.replace("ï", "&iuml;");

        return result;
    }
}