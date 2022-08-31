package com.sparta.billy.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessDto {
    private Boolean success;

    public static SuccessDto valueOf(String value){

        SuccessDto successDto = new SuccessDto();
        if ( value.equals("true")){ successDto.success = true; }
        else if ( value.equals("false") ){ successDto.success = false; }

        return successDto;
    }
}