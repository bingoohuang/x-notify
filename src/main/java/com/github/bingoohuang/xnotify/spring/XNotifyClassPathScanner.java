package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.xnotify.XNotifyProvider;
import lombok.val;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

public class XNotifyClassPathScanner extends ClassPathBeanDefinitionScanner {
    public XNotifyClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    /**
     * Configures parent scanner to search for the right interfaces. It can search
     * for all interfaces or just for those that extends a markerInterface or/and
     * those annotated with the annotationClass
     */
    public void registerFilters() {
        addExcludeFilter((metadataReader, metadataReaderFactory) -> !metadataReader.getClassMetadata().isInterface());
        addIncludeFilter(new AnnotationTypeFilter(XNotifyProvider.class));
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No Xnotifies was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            for (val holder : beanDefinitions) {
                val definition = (GenericBeanDefinition) holder.getBeanDefinition();

                if (logger.isDebugEnabled()) {
                    logger.debug("Creating XNotifyFactoryBean with name '" + holder.getBeanName()
                            + "' and '" + definition.getBeanClassName() + "' xnotifyInterface");
                }

                // the mapper interface is the original class of the bean
                // but, the actual class of the bean is MapperFactoryBean
                definition.getPropertyValues().add("xnotifyInterface", definition.getBeanClassName());
                definition.setBeanClass(XNotifyFactoryBean.class);
            }
        }

        return beanDefinitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping XNotifyFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' xnotifyInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }

}
