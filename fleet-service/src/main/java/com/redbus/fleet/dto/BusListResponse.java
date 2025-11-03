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
public class BusListResponse {
    private List<BusResponse> buses;
    private Long total_count;
    private Integer page;
    private Integer page_size;
}
