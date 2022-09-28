package com.turno.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
		
		public static ApplicationContext context;
		public static ApplicationContext getApplicationContext() {
			return context;
		}

		@Override
		public void setApplicationContext(ApplicationContext context) throws BeansException {
			ApplicationContextProvider.context = context;    
		}


		/**
		 * Returns the desired bean from spring application context.
		 * 
		 * @param clazz
		 * @return
		 */
		
		public static <T> T getBean( Class<T> clazz){
			return context.getBean(clazz);
		}
		
}
