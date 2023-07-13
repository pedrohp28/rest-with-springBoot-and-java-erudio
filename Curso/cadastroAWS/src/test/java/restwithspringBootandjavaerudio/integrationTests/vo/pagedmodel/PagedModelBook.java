package restwithspringBootandjavaerudio.integrationTests.vo.pagedmodel;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import restwithspringBootandjavaerudio.integrationTests.vo.BookVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;

import java.util.List;

@XmlRootElement
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookVOTest> content;

    public PagedModelBook() {}

    public List<BookVOTest> getContent() {
        return content;
    }

    public void setContent(List<BookVOTest> content) {
        this.content = content;
    }
}
