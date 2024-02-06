package com.globallogic.bcigloballogic.model.dto;

import com.globallogic.bcigloballogic.model.entity.PhoneEntity;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

    public Long number;
    public Integer citycode;
    public String countrycode;

    public static PhoneDto fromEntity(PhoneEntity entity) {
        PhoneDto dto = new PhoneDto();
        dto.setCitycode(entity.getCitycode());
        dto.setCountrycode(entity.getCountrycode());
        dto.setNumber(entity.getNumber());

        return dto;
    }
}
