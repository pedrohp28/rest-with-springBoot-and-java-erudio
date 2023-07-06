package restwithspringBootandjavaerudio.integrationTests.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement
public class BookVOTest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    public BookVOTest() {}

    public Long getId() {
        return id;
    }

    public void setId(Long key) {
        this.id = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookVOTest that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author) && Objects.equals(launchDate, that.launchDate) && Objects.equals(price, that.price) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, launchDate, price, title);
    }
}
