package restwithspringBootandjavaerudio.integrationTests.vo.pagedmodel;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;

import java.util.List;

@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonVOTest> content;

    public PagedModelPerson() {}

    public List<PersonVOTest> getContent() {
        return content;
    }

    public void setContent(List<PersonVOTest> content) {
        this.content = content;
    }
}
