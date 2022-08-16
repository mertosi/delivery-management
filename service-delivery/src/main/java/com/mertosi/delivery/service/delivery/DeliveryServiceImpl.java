package com.mertosi.delivery.service.delivery;

import com.mertosi.delivery.common.mapper.delivery.DeliveryResponseMapper;
import com.mertosi.delivery.feign.VehicleClient;
import com.mertosi.delivery.model.dto.request.delivery.MakeDeliveryRequest;
import com.mertosi.delivery.model.dto.response.VehicleResponse;
import com.mertosi.delivery.model.dto.response.delivery.MakeDeliveryResponse;
import com.mertosi.delivery.service.delivery.bag.DeliveryBagService;
import com.mertosi.delivery.service.delivery.shipment.DeliveryShipmentService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final VehicleClient vehicleClient;
    private final DeliveryBagService deliveryBagService;
    private final DeliveryShipmentService deliveryShipmentService;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    private final DeliveryResponseMapper mapper = Mappers.getMapper(DeliveryResponseMapper.class);

    @Override
    public MakeDeliveryResponse makeDelivery(MakeDeliveryRequest request) {
        VehicleResponse vehicleResponse = circuitBreakerFactory.create("getByLicensePlate").run(
                () -> vehicleClient.getByLicensePlate(request.getPlate()).getData(),
                t -> VehicleResponse.builder().licensePlate("34 TL 34").build());
        request.setPlate(vehicleResponse.getLicensePlate());

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
