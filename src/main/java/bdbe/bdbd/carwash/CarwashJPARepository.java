package bdbe.bdbd.carwash;

import bdbe.bdbd.bay.Bay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarwashJPARepository extends JpaRepository<Carwash, Long> {
    Carwash findFirstBy(); // 맨 처음 하나 찾기

    Carwash findBy();

    List<Carwash> findByCarwashId(Long carwashId);


}
