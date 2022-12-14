package com.mertosi.delivery.service.shipmentbag;

import com.mertosi.delivery.common.enums.ShipmentStatus;
import com.mertosi.delivery.common.exception.DeliveryException;
import com.mertosi.delivery.model.dto.request.ShipmentBagRequest;
import com.mertosi.delivery.model.entity.BagEntity;
import com.mertosi.delivery.model.entity.ShipmentBagEntity;
import com.mertosi.delivery.model.entity.ShipmentEntity;
import com.mertosi.delivery.repository.shipmentbag.ShipmentBagCommandRepository;
import com.mertosi.delivery.service.bag.BagQueryService;
import com.mertosi.delivery.service.shipment.ShipmentCommandService;
import com.mertosi.delivery.service.shipment.ShipmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentBagCommandServiceImpl implements ShipmentBagCommandService {

    private final ShipmentQueryService shipmentQueryService;
    private final ShipmentCommandService shipmentCommandService;
    private final BagQueryService bagQueryService;

    private final ShipmentBagCommandRepository shipmentBagCommandRepository;

    @Override
    public List<ShipmentBagEntity> create(List<ShipmentBagRequest> requests) {
        List<ShipmentBagEntity> shipmentBagEntities = new ArrayList<>();

        for (ShipmentBagRequest request : requests) {
            ShipmentEntity shipmentEntity = shipmentQueryService.getByBarcode(request.getBarcode());
            BagEntity bagEntity = bagQueryService.getByBarcode(request.getBagBarcode());
            compareDeliveryPoints(shipmentEntity, bagEntity);

            ShipmentBagEntity shipmentBagEntity = ShipmentBagEntity.builder()
                    .barcode(shipmentEntity)
                    .bagBarcode(bagEntity)
                    .build();

            ShipmentBagEntity savedShipmentBagEntity = shipmentBagCommandRepository.save(shipmentBagEntity);
            ShipmentEntity updatedBarcode = shipmentCommandService.updateStatus(shipmentEntity, ShipmentStatus.LOADED_INTO_BAG);
            savedShipmentBagEntity.setBarcode(updatedBarcode);
            shipmentBagEntities.add(savedShipmentBagEntity);
        }

        return shipmentBagEntities;
    }

    private void compareDeliveryPoints(ShipmentEntity barcode, BagEntity bagBarcode) {
        if (!barcode.getDeliveryPoint().equals(bagBarcode.getDeliveryPoint())) {
            throw new DeliveryException("Barcode and bag barcode delivery points are not same!");
        }
    }
}
