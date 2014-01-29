/*
 * Copyright 2014 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kie.mobile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.animation.AnimationHelper;
import javax.inject.Inject;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;

@EntryPoint
public class ShowcaseEntryPoint {
    
    @Inject
    private ClientFactory clientFactory;
    
    @AfterInitialization
    public void startApp() {
        hideLoadingPopup();
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                Window.alert("uncaught: " + e.getMessage());
                e.printStackTrace();
            }
        });
        new Timer() {

            @Override
            public void run() {
                start();

            }
        }.schedule(1);
    }

    private void start() {
        //set viewport and other settings for mobile
        MGWT.applySettings(MGWTSettings.getAppSetting());

        //build animation helper and attach it
        AnimationHelper animationHelper = new AnimationHelper();
        RootPanel.get().add(animationHelper);

        //animate
        animationHelper.goTo(clientFactory.getTaskListPresenter().getView(), Animation.SLIDE);
    }

    //Fade out the "Loading application" pop-up
    private void hideLoadingPopup() {
        final Element e = RootPanel.get("loading").getElement();

        new com.google.gwt.animation.client.Animation() {

            @Override
            protected void onUpdate(double progress) {
                e.getStyle().setOpacity(1.0 - progress);
            }

            @Override
            protected void onComplete() {
                e.getStyle().setVisibility(Style.Visibility.HIDDEN);
            }
        }.run(500);
    }

    public static native void redirect(String url)/*-{
     $wnd.location = url;
     }-*/;

}
