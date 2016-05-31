package cn.ifavor.networkutil.project.bean;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 19:38
 * @des ${TODO}
 */
public class User {
    private String origin;
    private String url;

    public User(String origin, String url) {
        this.origin = origin;
        this.url = url;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "User{" +
                "origin='" + origin + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
