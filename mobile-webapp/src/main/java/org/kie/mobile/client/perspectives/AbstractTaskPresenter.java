/*
 * Copyright 2014 JBoss by Red Hat.
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

package org.kie.mobile.client.perspectives;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.Caller;
import org.jbpm.console.ng.ht.service.TaskServiceEntryPoint;
import org.kie.mobile.client.ClientFactory;
import org.uberfire.security.Identity;

/**
 *
 * @author tlivora
 */
public abstract class AbstractTaskPresenter {
    
    public interface TaskView extends IsWidget {

        void displayNotification(String title, String text);
        
        HasTapHandlers getBackButton();
        
    }
    
    @Inject
    protected Caller<TaskServiceEntryPoint> taskServices;

    @Inject
    protected ClientFactory clientFactory;
    
    @Inject
    protected Identity identity;
    
}
