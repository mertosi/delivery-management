package com.mertosi.delivery.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryPointResponse extends AbstractResponse {
    private String deliveryPoint;
    private Integer value;
}
