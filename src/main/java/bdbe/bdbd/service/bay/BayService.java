package bdbe.bdbd.service.bay;


import bdbe.bdbd._core.exception.BadRequestError;
import bdbe.bdbd._core.exception.ForbiddenError;
import bdbe.bdbd._core.exception.NotFoundError;
import bdbe.bdbd.model.carwash.Carwash;
import bdbe.bdbd.repository.carwash.CarwashJPARepository;
import bdbe.bdbd.dto.bay.BayRequest;
import bdbe.bdbd.model.bay.Bay;
import bdbe.bdbd.model.member.Member;
import bdbe.bdbd.repository.bay.BayJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BayService {
    private final BayJPARepository bayJPARepository;
    private final CarwashJPARepository carwashJPARepository;

    public void createBay(BayRequest.SaveDTO dto, Long carwashId, Member member) {
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError("Carwash not found"));

        if (carwash.getMember().getId() != member.getId()) {
            throw new ForbiddenError("User is not the owner of the carwash.");
        }

        int bayNum = dto.getBayNum();
        boolean exists = bayJPARepository.findByCarwashId(carwashId)
                .stream()
                .anyMatch(bay -> bay.getBayNum() == bayNum);

        if (exists) {
            throw new BadRequestError("Bay number " + bayNum + " is already in use.");
        }

        Bay bay = dto.toBayEntity(carwash);

        bayJPARepository.save(bay);
    }


    public void changeStatus(Long bayId, int status, Member member) {
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new IllegalArgumentException("Bay not found"));

        if (bay.getCarwash().getMember().getId() != member.getId()) {
            throw new ForbiddenError("User is not the owner of the carwash bay.");
        }
        bay.changeStatus(status);
    }
}