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
package org.kie.mobile.ht.client.tasklist;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedHandler;
import com.googlecode.mgwt.ui.client.widget.celllist.HasCellSelectedHandler;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jbpm.console.ng.ht.model.TaskSummary;
import org.kie.mobile.ht.client.AbstractTaskPresenter;
import org.kie.mobile.ht.client.newtask.NewTaskPlace;
import org.kie.mobile.ht.client.taskdetails.TaskDetailsPlace;
import org.kie.mobile.ht.client.utils.TaskStatus;

/**
 *
 * @author livthomas
 * @author salaboy
 */
@ApplicationScoped
public class TaskListPresenter extends AbstractTaskPresenter {

    public interface TaskListView extends View {

        HasTapHandlers getNewTaskButton();

        HasRefresh getPullPanel();

        void setHeaderPullHandler(PullPanel.Pullhandler pullHandler);

        PullArrowWidget getPullHeader();

        void render(List<TaskSummary> tasks);

        HasCellSelectedHandler getTaskList();

    }

    @Inject
    private TaskListView view;

    public TaskListView getView() {
        return view;
    }

    private List<TaskSummary> taskList;

    @AfterInitialization
    public void init() {
        view.getNewTaskButton().addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                clientFactory.getPlaceController().goTo(new NewTaskPlace());
            }
        });

        view.getPullHeader().setHTML("pull down");

        PullArrowStandardHandler headerHandler = new PullArrowStandardHandler(view.getPullHeader(), view.getPullPanel());

        headerHandler.setErrorText("Error");
        headerHandler.setLoadingText("Loading");
        headerHandler.setNormalText("pull down");
        headerHandler.setPulledText("release to load");
        headerHandler.setPullActionHandler(new PullArrowStandardHandler.PullActionHandler() {
            @Override
            public void onPullAction(final AsyncCallback<Void> callback) {
                new Timer() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }.schedule(1000);

            }
        });
        view.setHeaderPullHandler(headerHandler);

        view.getTaskList().addCellSelectedHandler(new CellSelectedHandler() {
            @Override
            public void onCellSelected(CellSelectedEvent event) {
                clientFactory.getPlaceController().goTo(new TaskDetailsPlace(taskList.get(event.getIndex()).getId()));
            }
        });
    }

    public void refresh() {
        List<String> statuses = new ArrayList<String>();
        for (TaskStatus status : TaskStatus.values()) {
            statuses.add(status.toString());
        }
        taskServices.call(new RemoteCallback<List<TaskSummary>>() {
            @Override
            public void callback(List<TaskSummary> tasks) {
                taskList = tasks;
                view.render(taskList);
            }
        }).getTasksAssignedAsPotentialOwnerByExpirationDateOptional(identity.getName(), statuses, null, "en-UK");
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        refresh();
    }

}
