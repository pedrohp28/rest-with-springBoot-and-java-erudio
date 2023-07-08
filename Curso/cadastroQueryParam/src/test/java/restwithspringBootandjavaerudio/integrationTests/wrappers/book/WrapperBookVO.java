package restwithspringBootandjavaerudio.integrationTests.wrappers.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperBookVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedVo embedded;

    public WrapperBookVO() {
    }

    public BookEmbeddedVo getEmbedded() {
        return embedded;
    }

    public void setEmbedded(BookEmbeddedVo embedded) {
        this.embedded = embedded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrapperBookVO that)) return false;
        return Objects.equals(embedded, that.embedded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(embedded);
    }
}
