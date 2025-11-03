package com.redbus.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayoutListResponse {
    private Long busId;
    private Integer total_count;
    private List<SeatLayoutResponse> seats;
}
