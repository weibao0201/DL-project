package gov.nist.csd.acpt.util;

/**
 * @author sunyunjie (jaysunyun_361@163.com)
 * @date 2/23/17
 */
public class Property {

    private String source;
    private String subject;
    private String resource;
    private boolean match;

    public Property() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

}
