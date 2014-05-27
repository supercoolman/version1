package se.mah.kd330a.project.links;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class MainList {
    @ElementList
    public List<LinkList> list;
    
    @Root
    public static class LinkList {
        
        @Attribute (required = false)
        public String title = null;

        @ElementList (required = false)
        public List<LinkObject> linkobjectlist;
        
            @Root
            public static class LinkObject {
    
                @Attribute (required = false)
                public String title = null;
    
                @Element (required = false)
                public String url = null;
    
                @Element (required = false)
                public String telnr = null;
  
                @Element (required = false)
                public String subtitle = null;
                
                @Element (required = false)
                public String icon = null;
            }
    }
}
