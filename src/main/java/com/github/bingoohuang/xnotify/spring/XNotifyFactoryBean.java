package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.xnotify.impl.XNotifyFactory;
import lombok.Setter;
import lombok.val;
import org.n3r.eql.eqler.generators.ActiveProfilesThreadLocal;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class XNotifyFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    @Setter private Class<T> xnotifyInterface;
    @Setter private ApplicationContext applicationContext;

    @Override
    public T getObject() {
        val activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        ActiveProfilesThreadLocal.set(activeProfiles);
        return XNotifyFactory.create(xnotifyInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.xnotifyInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
