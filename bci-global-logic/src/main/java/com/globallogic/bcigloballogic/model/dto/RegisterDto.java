package com.globallogic.bcigloballogic.model.dto;

import com.globallogic.bcigloballogic.model.entity.PhoneEntity;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;

}
