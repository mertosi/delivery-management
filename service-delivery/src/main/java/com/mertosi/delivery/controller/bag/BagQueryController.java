package com.mertosi.delivery.controller.bag;

import com.mertosi.delivery.common.mapper.bag.BagResponseMapper;
import com.mertosi.delivery.model.dto.response.BagResponse;
import com.mertosi.delivery.model.dto.response.BaseResponse;
import com.mertosi.delivery.service.bag.BagQueryService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bag")
@Validated
public class BagQueryController {
    private final BagQueryService bagQueryService;
    private final BagResponseMapper mapper = Mappers.getMapper(BagResponseMapper.class);

    @GetMapping("/{barcode}")
    public BaseResponse<BagResponse> getByBarcode(@PathVariable @NotBlank String barcode) {
        return BaseResponse.<BagResponse>builder()
                .data(mapper.map(bagQueryService.getByBarcode(barcode)))
                .build();
    }
}
