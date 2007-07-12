package x360mediaserver.service;

public class ToasterOptions
{
    String  title                = "default title";
    int     width                = 300;
    int     height               = 280;
    boolean blockExecutionOnOpen = true;

    public ToasterOptions()
    {}

    public ToasterOptions(String title)
    {
        this(title, null, null, null);
    }

    public ToasterOptions(String title, Integer width, Integer height,
            Boolean blockExecutionOnOpen)
    {
        if (title != null)
            this.title = title;
        if (width != null)
            this.width = width;
        if (height != null)
            this.height = height;
        if (blockExecutionOnOpen != null)
            this.blockExecutionOnOpen = blockExecutionOnOpen;
    }
}