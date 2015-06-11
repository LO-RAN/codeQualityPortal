package com.compuware.caqs.service.delegationsvc;

import org.springframework.context.ApplicationContext;

/**
 *
 * @author cwfr-fdubois
 */
public class BusinessFactory {

    /**
     * The application context.
     */
    private static ApplicationContext appContext;

    /**
     * The singleton instance.
     */
    private static BusinessFactory instance = new BusinessFactory();

    /**
     * Provate constructor of the singleton.
     */
    private BusinessFactory() {
    }

    /**
     * Factory accessor.
     * @return the unique business factory instance.
     */
    public static BusinessFactory getInstance() {
        return instance;
    }

    /**
     * Initialize the application context.
     * @param ctx the new application context.
     */
    public static void init(ApplicationContext ctx) {
        appContext = ctx;
    }

    /**
     * Get the bean for the given configuration name.
     * @param name the configuration name as defined in the configuration file.
     * @return the object created by the factory.
     */
    public Object getBean(String name) {
        return appContext.getBean(name);
    }
}
