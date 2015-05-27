/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.ashley.core.Entity;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBar;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UISpriteAnimationItemProperties;
import com.uwsoft.editor.renderer.factory.EntityFactory;

import java.util.Set;

/**
 * Created by azakhary on 5/12/2015.
 */
public class EditSpriteAnimationDialogMediator extends SimpleMediator<EditSpriteAnimationDialog> {
    private static final String TAG = EditSpriteAnimationDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    private Entity observable = null;

    public EditSpriteAnimationDialogMediator() {
        super(NAME, new EditSpriteAnimationDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        viewComponent.setEmpty("No item selected");
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.ITEM_SELECTION_CHANGED,
                Overlap2D.EMPTY_SPACE_CLICKED,
                UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED,
                EditSpriteAnimationDialog.ADD_BUTTON_PRESSED,
                EditSpriteAnimationDialog.DELETE_BUTTON_PRESSED,
                Overlap2DMenuBar.SPRITE_ANIMATIONS_EDITOR_OPEN
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        switch (notification.getName()) {
            case Overlap2DMenuBar.SPRITE_ANIMATIONS_EDITOR_OPEN:
                viewComponent.show(uiStage);
                break;
            case UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED:
                viewComponent.show(uiStage);
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    Entity entity = selection.iterator().next();
                    if(entity.flags == EntityFactory.SPRITE_TYPE) {
                        setObservable(entity);
                    } else {
                        observable = null;
                        viewComponent.setEmpty("Selected item is not a sprite animation");
                    }
                }

            break;
            case Overlap2D.EMPTY_SPACE_CLICKED:
                setObservable(null);
                break;
            case EditSpriteAnimationDialog.ADD_BUTTON_PRESSED:
                addAnimation();
                updateView();
            break;
            case EditSpriteAnimationDialog.DELETE_BUTTON_PRESSED:
                removeAnimation(notification.getBody());
                updateView();
            break;
        }
    }

    private void setObservable(Entity animation) {
        observable = animation;
        updateView();
        viewComponent.setName("");
        viewComponent.setFrameFrom(0);
        viewComponent.setFrameTo(0);
    }

    private void updateView() {
        if(observable == null) {
            viewComponent.setEmpty("No item selected");
        } else {
        	//TODO fix and uncomment
//            Map<String, SpriteAnimation.Animation> animations = observable.getAnimations();
//            viewComponent.updateView(animations);
        }
    }

    private void addAnimation() {
        String name = viewComponent.getName();
        int frameFrom = viewComponent.getFrameFrom();
        int frameTo = viewComponent.getFrameTo();
        //TODO fix and uncomment
//        observable.getAnimations().put(name, new SpriteAnimation.Animation(frameFrom, frameTo, name));
//        observable.updateDataVO();
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED, observable);
    }

    private void removeAnimation(String name) {
    	 //TODO fix and uncomment
//        observable.getAnimations().remove(name);
//        observable.updateDataVO();
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED, observable);
    }
}
