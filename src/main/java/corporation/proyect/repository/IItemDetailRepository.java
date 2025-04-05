package corporation.proyect.repository;

import corporation.proyect.entity.ItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemDetailRepository extends JpaRepository<ItemDetail,Integer> {
}
