package com.LMS.Pulse.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResultDto {
    // **THE FIX**: Changed the data type from Long to String.
    private String userId;
    private String username;
    private double score;
}
