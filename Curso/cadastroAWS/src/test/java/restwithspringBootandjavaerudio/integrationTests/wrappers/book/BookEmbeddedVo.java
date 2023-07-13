package restwithspringBootandjavaerudio.integrationTests.wrappers.book;


import com.fasterxml.jackson.annotation.JsonProperty;
import restwithspringBootandjavaerudio.integrationTests.vo.BookVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BookEmbeddedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bookVOList")
    private List<BookVOTest> people;

    public BookEmbeddedVo() {}

    public List<BookVOTest> getPeople() {
        return people;
    }

    public void setPeople(List<BookVOTest> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEmbeddedVo that)) return false;
        return Objects.equals(people, that.people);
    }

    @Override
    public int hashCode() {
        return Objects.hash(people);
    }
}
