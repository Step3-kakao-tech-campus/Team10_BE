package bdbe.bdbd.carwash;


import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

//@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CarwashService {
    private final CarwashJPARepository carwashJPARepository;
    private final KeywordJPARepository keywordJPARepository;
    private final LocationJPARepository locationJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final CarwashKeywordJPARepository carwashKeywordJPARepository;

    public List<CarwashResponse.FindAllDTO> findAll(int page) {
        // Pageable 검증
        if (page < 0) {
            throw new IllegalArgumentException("Invalid page number.");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Carwash> carwashEntities = carwashJPARepository.findAll(pageable);

        // Page 객체 검증
        if (carwashEntities == null || !carwashEntities.hasContent()) {
            throw new NoSuchElementException("No carwash entities found.");
        }

        List<CarwashResponse.FindAllDTO> carwashResponses = carwashEntities.getContent().stream()
                .map(CarwashResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        // List 객체 검증
        if (carwashResponses == null || carwashResponses.isEmpty()) {
            throw new NoSuchElementException("No carwash entities transformed.");
        }

        return carwashResponses;
    }

    @Transactional
    public void save(CarwashRequest.SaveDTO saveDTO, User sessionUser) {
        // 별점은 리뷰에서 계산해서 넣어주기
        // 지역
        Location location = saveDTO.toLocationEntity();
        locationJPARepository.save(location);
        // 세차장
        Carwash carwash = saveDTO.toCarwashEntity(location, sessionUser);
        carwashJPARepository.save(carwash);
        // 운영시간
        List<Optime> optimes = saveDTO.toOptimeEntities(carwash);
        optimeJPARepository.saveAll(optimes);
        // 키워드
        List<Long> keywordIdList = saveDTO.getKeywordId();
        List<CarwashKeyword> carwashKeywordList = new ArrayList<>();
        for (Long keywordId : keywordIdList) {
            Keyword keyword = keywordJPARepository.findById(keywordId)
                    .orElseThrow(() -> new IllegalArgumentException("Keyword not found"));
            //carwash-keyword 다대다 매핑
            CarwashKeyword carwashKeyword = CarwashKeyword.builder().carwash(carwash).keyword(keyword).build();
            carwashKeywordList.add(carwashKeyword);
        }
        carwashKeywordJPARepository.saveAll(carwashKeywordList);
    } //변경감지, 더티체킹, flush, 트랜잭션 종료

    public CarwashResponse.FindByIdDTO getfindById(Long carwashId) {

        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("not found carwash"));

        return new CarwashResponse.FindByIdDTO(carwash);


    }
}
