package company.test.dto;

/**
 * Created by wkolac200 on 9/17/2015.
 */
public class Greeting {

    private String id;
    private String content;

    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public String toString() {
        return "id=".concat(id).concat(" content=").concat(content);
    }
}
