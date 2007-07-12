package x360mediaserver;

public class ConfigStream
{
    public String url  = null;
    public String name = null;
    public int    type = -1;

    public ConfigStream()
    {}

    public ConfigStream(String url, String name, int type)
    {
        this.url = url;
        this.name = name;
        this.type = type;
    }
}
