/*
 * Copyright 2012 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.services.task.audit.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.audit.JPATaskLifeCycleEventListener;
import org.jbpm.services.task.lifecycle.listeners.BAMTaskEventListener;
import org.junit.After;
import org.junit.Before;
import org.kie.internal.task.api.InternalTaskService;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.hibernate.search.jpa.Search;
import org.jbpm.services.task.HumanTaskConfigurator;
import org.jbpm.services.task.audit.TaskAuditServiceFactory;

/**
 *
 *
 */

public class SearchLocalTaskAuditTest extends TaskAuditBaseTest {

	private PoolingDataSource pds;
	private EntityManagerFactory emf;
	
	@Before
	public void setup() {
		pds = setupPoolingDataSource();
		emf = Persistence.createEntityManagerFactory( "org.jbpm.services.task" );
                
		HumanTaskConfigurator configurator = HumanTaskServiceFactory.newTaskServiceConfigurator()
												.entityManagerFactory(emf)
												.listener(new JPATaskLifeCycleEventListener(true))
												.listener(new BAMTaskEventListener(true));
												
                this.taskService = (InternalTaskService) configurator.getTaskService();
                
                
                fullTextEntityManager = Search.getFullTextEntityManager(emf.createEntityManager());
                
                
                
                this.taskAuditService = TaskAuditServiceFactory.newTaskAuditServiceConfigurator().setTaskService(taskService).getTaskAuditService();
	}
	
	@After
	public void clean() {
		if (emf != null) {
			emf.close();
		}
		if (pds != null) {
			pds.close();
		}
	}
}
