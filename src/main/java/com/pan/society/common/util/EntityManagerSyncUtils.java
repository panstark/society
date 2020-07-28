package com.pan.society.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * EntityManager线程同步工具类。
 *
 * @author wangruiv
 * @date 2017-12-25 09:47:59
 */
public class EntityManagerSyncUtils {
    private ApplicationContext applicationContext;

    private EntityManagerFactory entityManagerFactory;

    public EntityManagerSyncUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 当前线程绑定EntityManager，使用完后需要调用unbindResource()释放EntityManager
     *
     * @return 通过工厂创建的EntityManager。
     */
    public EntityManager bindResource() {
        entityManagerFactory = applicationContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityManagerHolder entityManagerHolder = new EntityManagerHolder(entityManager);
        TransactionSynchronizationManager.bindResource(entityManagerFactory, entityManagerHolder);

        return entityManager;
    }

    /**
     * 释放EntityManager
     */
    public void unbindResource() {
        EntityManagerHolder entityManagerHolder =
                (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
        EntityManagerFactoryUtils.closeEntityManager(entityManagerHolder.getEntityManager());
    }
}
