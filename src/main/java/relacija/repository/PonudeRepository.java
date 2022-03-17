package relacija.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import relacija.domain.Ponude;

/**
 * Spring Data SQL repository for the Ponude entity.
 */
@Repository
public interface PonudeRepository extends JpaRepository<Ponude, Long> {
    default Optional<Ponude> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ponude> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ponude> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ponude from Ponude ponude left join fetch ponude.ponudjaci",
        countQuery = "select count(distinct ponude) from Ponude ponude"
    )
    Page<Ponude> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct ponude from Ponude ponude left join fetch ponude.ponudjaci")
    List<Ponude> findAllWithToOneRelationships();

    @Query("select ponude from Ponude ponude left join fetch ponude.ponudjaci where ponude.id =:id")
    Optional<Ponude> findOneWithToOneRelationships(@Param("id") Long id);
}
