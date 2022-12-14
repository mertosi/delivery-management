package com.mertosi.delivery.repository.deliverypoint;

import com.mertosi.delivery.model.entity.DeliveryPointEntity;
import com.mertosi.delivery.model.entity.DeliveryPointEntityBuilder;
import com.mertosi.delivery.repository.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryPointCommandRepositoryTest extends AbstractJpaIntegrationTest {

    @Autowired
    private DeliveryPointCommandRepository deliveryPointCommandRepository;

    private DeliveryPointEntity testDeliveryPointEntity;

    @BeforeEach
    void setUp() {
        testDeliveryPointEntity = DeliveryPointEntityBuilder.getValidDeliveryPointEntityWithoutRelations().build();
    }

    @Test
    void givenValidDeliveryPointEntity_whenCreateDeliveryPointEntity_thenReturnDeliveryPointEntity() {
        DeliveryPointEntity savedDeliveryPointEntity = deliveryPointCommandRepository.save(testDeliveryPointEntity);
        Optional<DeliveryPointEntity> deliveryPointEntity = deliveryPointCommandRepository.findById(savedDeliveryPointEntity.getId());
        assertThat(deliveryPointEntity).contains(savedDeliveryPointEntity);
    }
}
