package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BayResponse {
    @Getter
    @Setter
    public static class FindAllDTO {

        private Long id;
        private String des;
        private String name;
        private double rate;
        private String tel;
        private Long rId;
        private Long userId;


        public FindAllDTO(Carwash carwash) {
            this.id = carwash.getId();
            this.des = carwash.getDes();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.tel = carwash.getTel();
            this.rId = carwash.getRegion().getId();
            this.userId = carwash.getUser().getId();
        }
    }
    @Getter
    @Setter
    public class KeywordResponseDTO {
        private String keywordName;

        public KeywordResponseDTO(String keywordName) {
            this.keywordName = keywordName;
        }

        // getters, setters
    }

    @Getter
    @Setter
    public static class getBayById{

        private Long id;
        private Long cid;
        private int bayNum;
        private int bayType;
        private int status;
        private List<Reservation> reservation;

        public getBayById(Bay bay) {
            this.id = bay.getId();
            this.cid = bay.getCarwash().getId();
            this.bayNum = bay.getBayNum();
            this.status = bay.getStatus();
            this.reservation = bay.getReservationList();
        }
    }
}