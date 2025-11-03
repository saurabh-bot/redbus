package com.redbus.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBusesResponse {
    private Long total_count;
    private List<BusSearchResult> buses;
    private Integer page;
    private Integer page_size;
}
