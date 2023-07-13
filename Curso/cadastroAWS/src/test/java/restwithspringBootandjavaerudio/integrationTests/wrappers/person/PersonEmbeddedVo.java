package restwithspringBootandjavaerudio.integrationTests.wrappers.person;


import com.fasterxml.jackson.annotation.JsonProperty;
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbeddedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("personVOList")
    private List<PersonVOTest> people;

    public PersonEmbeddedVo() {}

    public List<PersonVOTest> getPeople() {
        return people;
    }

    public void setPeople(List<PersonVOTest> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonEmbeddedVo that)) return false;
        return Objects.equals(people, that.people);
    }

    @Override
    public int hashCode() {
        return Objects.hash(people);
    }
}
