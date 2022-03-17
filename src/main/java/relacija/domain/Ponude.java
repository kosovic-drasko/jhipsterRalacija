package relacija.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ponude.
 */
@Entity
@Table(name = "ponude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ponude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sifra_postupka")
    private Integer sifraPostupka;

    @Column(name = "naziv_lijeka")
    private String nazivLijeka;

    @Column(name = "iznos")
    private String iznos;

    @OneToOne
    @JoinColumn(unique = true)
    private Ponudjaci ponudjaci;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ponude id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSifraPostupka() {
        return this.sifraPostupka;
    }

    public Ponude sifraPostupka(Integer sifraPostupka) {
        this.setSifraPostupka(sifraPostupka);
        return this;
    }

    public void setSifraPostupka(Integer sifraPostupka) {
        this.sifraPostupka = sifraPostupka;
    }

    public String getNazivLijeka() {
        return this.nazivLijeka;
    }

    public Ponude nazivLijeka(String nazivLijeka) {
        this.setNazivLijeka(nazivLijeka);
        return this;
    }

    public void setNazivLijeka(String nazivLijeka) {
        this.nazivLijeka = nazivLijeka;
    }

    public String getIznos() {
        return this.iznos;
    }

    public Ponude iznos(String iznos) {
        this.setIznos(iznos);
        return this;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }

    public Ponudjaci getPonudjaci() {
        return this.ponudjaci;
    }

    public void setPonudjaci(Ponudjaci ponudjaci) {
        this.ponudjaci = ponudjaci;
    }

    public Ponude ponudjaci(Ponudjaci ponudjaci) {
        this.setPonudjaci(ponudjaci);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ponude)) {
            return false;
        }
        return id != null && id.equals(((Ponude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ponude{" +
            "id=" + getId() +
            ", sifraPostupka=" + getSifraPostupka() +
            ", nazivLijeka='" + getNazivLijeka() + "'" +
            ", iznos='" + getIznos() + "'" +
            "}";
    }
}
