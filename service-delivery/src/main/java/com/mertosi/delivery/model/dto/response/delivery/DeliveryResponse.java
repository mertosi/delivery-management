package com.mertosi.delivery.model.dto.response.delivery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mertosi.delivery.common.enums.DeliveryType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@JsonIgnoreProperties(value = {"getType", "isDeliveryBag"})
public class DeliveryResponse {
    private String barcode;
    private Integer state;

    public DeliveryType getType() {
        if (isBag()) {
            return DeliveryType.BAG;
        }
        return DeliveryType.SHIPMENT;
    }

    public boolean isBag() {
        return StringUtils.hasText(barcode) && barcode.startsWith("C") && barcode.length() == 7;
    }
}
