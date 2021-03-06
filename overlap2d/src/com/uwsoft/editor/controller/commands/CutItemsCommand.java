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

package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.commons.MsgAPI;
import com.uwsoft.editor.renderer.data.CompositeVO;

import java.util.Set;

/**
 * Created by azakhary on 4/28/2015.
 */
public class CutItemsCommand extends EntityModifyRevertableCommand {

    private String backup;

    @Override
    public void doAction() {
        backup = CopyItemsCommand.getJsonStringFromEntities(sandbox.getSelector().getSelectedItems());
        String data = backup;

        Object[] payload = new Object[2];
        payload[0] = sandbox.getCamera().position.cpy();
        payload[1] = data;
        sandbox.copyToClipboard(payload);
        sandbox.getSelector().removeCurrentSelectedItems();

        facade.sendNotification(DeleteItemsCommand.DONE);
    }

    @Override
    public void undoAction() {
        Json json = new Json();
        CompositeVO compositeVO = json.fromJson(CompositeVO.class, backup);
        Set<Entity> newEntitiesList = PasteItemsCommand.createEntitiesFromVO(compositeVO);

        for (Entity entity : newEntitiesList) {
            facade.sendNotification(MsgAPI.NEW_ITEM_ADDED, entity);
        }

        sandbox.getSelector().setSelections(newEntitiesList, true);
    }
}
