package com.mertosi.deliverymanagement.service.delivery;

import com.mertosi.deliverymanagement.common.mapper.delivery.DeliveryResponseMapper;
import com.mertosi.deliverymanagement.model.dto.request.delivery.MakeDeliveryRequest;
import com.mertosi.deliverymanagement.model.dto.response.delivery.MakeDeliveryResponse;
import com.mertosi.deliverymanagement.service.delivery.bag.DeliveryBagService;
import com.mertosi.deliverymanagement.service.delivery.shipment.DeliveryShipmentService;
import com.mertosi.deliverymanagement.service.vehicle.VehicleQueryService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final VehicleQueryService vehicleQueryService;
    private final DeliveryBagService deliveryBagService;
    private final DeliveryShipmentService deliveryShipmentService;

    private final DeliveryResponseMapper mapper = Mappers.getMapper(DeliveryResponseMapper.class);

    @Override
    public MakeDeliveryResponse makeDelivery(MakeDeliveryRequest request) {
        vehicleQueryService.getByLicensePlate(request.getPlate());
        MakeDeliveryResponse response = mapper.map(request);

        response.getRoute().forEach(route -> route.getDeliveries().forEach(delivery -> {
            if (delivery.isDeliveryShipment()) {
                deliveryShipmentService.delivery(route, delivery);
            } else if (delivery.isDeliveryBag()) {
                deliveryBagService.delivery(route, delivery);
            }
        }));

        deliveryBagService.checkBagsStatusAfterDelivery();
        return response;
    }
}
