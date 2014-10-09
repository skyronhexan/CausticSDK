
package com.teotigraphix.gdx.groove.ui.behavior;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.app.IApplicationState;
import com.teotigraphix.gdx.groove.ui.components.ModePane;
import com.teotigraphix.gdx.groove.ui.components.ModePaneListener;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;
import com.teotigraphix.gdx.groove.ui.model.MainMode;
import com.teotigraphix.gdx.groove.ui.model.UIModel.UIModelEvent;
import com.teotigraphix.gdx.groove.ui.model.UIModel.UIModelEventKind;

public class ModePaneBehavior extends CaustkBehavior {

    @Inject
    private IApplicationState applicationState;

    private ModePane view;

    public ModePaneBehavior() {
    }

    @Override
    public void onAwake() {
        super.onAwake();
        applicationState.getUI().getEventBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        applicationState.getUI().getEventBus().unregister(this);
    }

    public ModePane create() {
        view = new ModePane(getSkin(), applicationState.getUI().getMainModes());
        view.addListener(new ModePaneListener() {
            @Override
            public void selectedIndexChange(ModePaneEvent event, int index) {
                getApplication().getLogger().debug("",
                        "MainMode Change " + MainMode.fromIndex(index).getLabel());
                applicationState.getUI().setMainMode(MainMode.fromIndex(index));
            }
        });

        view.create(StylesDefault.ModePane);

        return view;
    }

    @Subscribe
    public void OnUIModelImplEvent(UIModelEvent event) {
        if (event.getKind() == UIModelEventKind.MainModeChange) {
            view.select(event.getModel().getMainMode().getIndex());
        }
    }
}
