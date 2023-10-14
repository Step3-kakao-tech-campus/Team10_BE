package bdbe.bdbd.carwash;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class CarwashRestController {

    private final CarwashService carwashService;

    // 전체 세차장 목록 조회, 10개씩 페이징
    @GetMapping("/carwashes/recommended")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<CarwashResponse.FindAllDTO> dtos = carwashService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(dtos);
        return ResponseEntity.ok(apiResult);
    }

    //세차장 등록
    @PostMapping("/owner/carwashes/register")
    public ResponseEntity<?> save(@RequestBody @Valid CarwashRequest.SaveDTO saveDTOs, Errors errors,  @AuthenticationPrincipal CustomUserDetails userDetails) {
        carwashService.save(saveDTOs, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/carwashes/{carwash_id}/introduction") //세차장 상세 정보 조회
    public ResponseEntity<?> findById(@PathVariable("carwash_id") Long carwashId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        carwashService.getfindById(carwashId, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PutMapping("/owner/carwashes/{carwash_id}/details") //세차장 정보 수정
        public ResponseEntity<?> updateCarwashDetails(@PathVariable("carwash_id") Long carwashId, @AuthenticationPrincipal CustomUserDetails userDetails) {
            carwashService.updateCarwashDetails(carwashId, userDetails.getUser());
            return ResponseEntity.ok(ApiUtils.success(null));

    }

}