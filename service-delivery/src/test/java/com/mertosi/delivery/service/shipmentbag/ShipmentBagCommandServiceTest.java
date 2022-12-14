package com.mertosi.delivery.service.shipmentbag;

import com.mertosi.delivery.common.enums.ShipmentStatus;
import com.mertosi.delivery.common.exception.DeliveryException;
import com.mertosi.delivery.model.dto.request.ShipmentBagRequest;
import com.mertosi.delivery.model.dto.request.ShipmentBagRequestBuilder;
import com.mertosi.delivery.model.entity.*;
import com.mertosi.delivery.repository.shipmentbag.ShipmentBagCommandRepository;
import com.mertosi.delivery.service.AbstractUnitTest;
import com.mertosi.delivery.service.bag.BagQueryService;
import com.mertosi.delivery.service.shipment.ShipmentCommandService;
import com.mertosi.delivery.service.shipment.ShipmentQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ShipmentBagCommandServiceTest extends AbstractUnitTest {

    @InjectMocks
    ShipmentBagCommandServiceImpl shipmentBagCommandService;

    @Mock
    private ShipmentQueryService shipmentQueryService;
    @Mock
    private ShipmentCommandService shipmentCommandService;
    @Mock
    private BagQueryService bagQueryService;
    @Mock
    private ShipmentBagCommandRepository shipmentBagCommandRepository;

    @Test
    void givenValidShipmentBagRequest_whenCreateShipmentBagEntity_thenReturnShipmentBagEntity() {
        List<ShipmentBagRequest> testShipmentBagRequests = ShipmentBagRequestBuilder.getValidShipmentBagRequests();
        ShipmentBagEntity testShipmentBagEntity = ShipmentBagEntityBuilder.getValidShipmentBagEntity();
        DeliveryPointEntity testDeliveryPointEntity = DeliveryPointEntityBuilder.getValidDeliveryPointEntity();

        ShipmentEntity testShipmentEntity = testShipmentBagEntity.getBarcode();
        testShipmentEntity.setDeliveryPoint(testDeliveryPointEntity);
        BagEntity testBagEntity = testShipmentBagEntity.getBagBarcode();
        testBagEntity.setDeliveryPoint(testDeliveryPointEntity);

        when(shipmentQueryService.getByBarcode(anyString())).thenReturn(testShipmentEntity);
        when(bagQueryService.getByBarcode(anyString())).thenReturn(testBagEntity);
        when(shipmentBagCommandRepository.save(any(ShipmentBagEntity.class))).thenReturn(testShipmentBagEntity);
        when(shipmentCommandService.updateStatus(any(ShipmentEntity.class), any(ShipmentStatus.class))).thenReturn(testShipmentEntity);

        List<ShipmentBagEntity> shipmentBagEntities = shipmentBagCommandService.create(testShipmentBagRequests);

        assertThat(shipmentBagEntities).isNotEmpty();
    }

    @Test
    void givenInvalidDeliveryPoints_whenCreateShipmentBagEntity_thenThrowDeliveryException() {
        List<ShipmentBagRequest> testShipmentBagRequests = ShipmentBagRequestBuilder.getValidShipmentBagRequests();
        ShipmentEntity testShipmentEntity = ShipmentEntityBuilder.getValidShipmentEntity();
        BagEntity testBagEntity = BagEntityBuilder.getValidBagEntity();

        when(shipmentQueryService.getByBarcode(anyString())).thenReturn(testShipmentEntity);
        when(bagQueryService.getByBarcode(anyString())).thenReturn(testBagEntity);

        assertThrows(DeliveryException.class, () -> shipmentBagCommandService.create(testShipmentBagRequests));
    }
}
