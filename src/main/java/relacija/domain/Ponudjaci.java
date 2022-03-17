package relacija.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ponudjaci.
 */
@Entity
@Table(name = "ponudjaci")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ponudjaci implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "naziv_ponudjaca")
    private String nazivPonudjaca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ponudjaci id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazivPonudjaca() {
        return this.nazivPonudjaca;
    }

    public Ponudjaci nazivPonudjaca(String nazivPonudjaca) {
        this.setNazivPonudjaca(nazivPonudjaca);
        return this;
    }

    public void setNazivPonudjaca(String nazivPonudjaca) {
        this.nazivPonudjaca = nazivPonudjaca;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ponudjaci)) {
            return false;
        }
        return id != null && id.equals(((Ponudjaci) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ponudjaci{" +
            "id=" + getId() +
            ", nazivPonudjaca='" + getNazivPonudjaca() + "'" +
            "}";
    }
}
