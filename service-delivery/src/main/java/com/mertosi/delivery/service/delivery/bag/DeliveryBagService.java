package com.mertosi.delivery.service.delivery.bag;

import com.mertosi.delivery.model.entity.BagEntity;
import com.mertosi.delivery.service.delivery.Delivery;

public interface DeliveryBagService extends Delivery {
    boolean isLoadable(BagEntity bagEntity, Integer routeDeliveryPoint);

    void checkBagsStatusAfterDelivery();
}
