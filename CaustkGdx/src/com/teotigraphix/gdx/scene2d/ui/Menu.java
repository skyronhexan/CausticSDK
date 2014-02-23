
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.teotigraphix.gdx.scene2d.ui.MenuBar.MenuBarStyle;
import com.teotigraphix.gdx.scene2d.ui.MenuBar.MenuItem;

public class Menu extends Dialog {

    private Array<MenuItem> menuItems;

    private MenuBarStyle menuBarStyle;

    private Skin skin;

    private Tooltip tooltip;

    private AdvancedList<MenuRowRenderer> list;

    public Menu(Array<MenuItem> menuItems, MenuBarStyle menuBarStyle, Skin skin) {
        super("", menuBarStyle.windowStyle);
        this.menuItems = menuItems;
        this.menuBarStyle = menuBarStyle;
        this.skin = skin;
        setModal(false);
        initialize();
    }

    private void initialize() {

        list = new AdvancedList<MenuRowRenderer>(menuItems.toArray(), MenuRowRenderer.class, skin,
                menuBarStyle.rowRendererStyle);
        list.setItems(menuItems.toArray());
        getContentTable().add(list);

        list.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof AdvancedListChangeEvent) {
                    //AdvancedList<?> list = (AdvancedList<?>)event.getListenerActor();
                    int selectedIndex = ((AdvancedListChangeEvent)event).getSelectedIndex();
                    //System.out.println(selectedIndex);
                    if (selectedIndex == -1) {
                        stopTooltip();
                    } else {
                        startTooltip(selectedIndex);
                    }
                }
                return false;
            }
        });

        list.addCaptureListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                touchMove((AdvancedList<?>)event.getListenerActor(), y);
                return super.mouseMoved(event, x, y);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                AdvancedList<?> list = (AdvancedList<?>)event.getListenerActor();
                list.setSelectedIndex(-1);
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AdvancedList<?> listenerActor = (AdvancedList<?>)event.getListenerActor();
                Array<MenuItem> items = (Array<MenuItem>)listenerActor.getUserObject();
                MenuItem menuItem = items.get(listenerActor.getSelectedIndex());
                itemClick(menuItem);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        list.setSelectedIndex(-1);
    }

    protected void stopTooltip() {
        clearActions();
        if (tooltip == null)
            return;
        tooltip.hide();
        tooltip = null;
    }

    protected void startTooltip(int selectedIndex) {
        addAction(Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                showTooltip();
            }
        })));
    }

    protected void showTooltip() {
        if (tooltip != null)
            return;
        if (list.getSelectedIndex() == -1)
            return;

        MenuItem menuItem = menuItems.get(list.getSelectedIndex());
        tooltip = Tooltip.show(getStage(), skin, menuItem.getHelpText());
        tooltip.setPosition(Gdx.input.getX() + 20f, getStage().getHeight() - Gdx.input.getY()
                - tooltip.getHeight() - 20f);
    }

    protected void itemClick(MenuItem menuItem) {
        System.out.println("ItemClick : " + menuItem);
    }

    void touchMove(AdvancedList<?> list, float y) {
        int oldIndex = list.getSelectedIndex();
        int selectedIndex = (int)((list.getHeight() - y) / list.getItemHeight());
        selectedIndex = Math.max(0, selectedIndex);
        selectedIndex = Math.min(list.getItems().length - 1, selectedIndex);
        if (oldIndex != selectedIndex) {
            list.setSelectedIndex(selectedIndex);
            list.invalidate();
            list.validate();
        }
        //        if (oldIndex != selectedIndex) {
        //            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
        //            if (fire(changeEvent)) selectedIndex = oldIndex;
        //            Pools.free(changeEvent);
        //        }
    }
}
