package com.sion.concertbooking.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestDataCleaner {

    private final EntityManager entityManager;

    public TestDataCleaner(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void cleanUp() {
        entityManager.getMetamodel().getEntities().forEach(entityType -> {
            Class<?> entityClazz = entityType.getJavaType();
            if (entityClazz.isAnnotationPresent(Table.class)) {
                Table tableAnnotation = entityClazz.getAnnotation(Table.class);
                String tableName = tableAnnotation.name();
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            }
        });
    }
}