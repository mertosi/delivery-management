package com.mertosi.delivery.model.dto.response.delivery;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MakeDeliveryResponse {
    private String plate;
    private List<RouteResponse> route;
}
