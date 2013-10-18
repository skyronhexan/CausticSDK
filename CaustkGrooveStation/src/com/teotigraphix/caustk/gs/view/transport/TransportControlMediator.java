
package com.teotigraphix.caustk.gs.view.transport;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerTransportChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup;
import com.teotigraphix.libgdx.ui.caustk.TransportGroup.OnTransportGroupListener;

public class TransportControlMediator extends ScreenMediator {

    private TransportGroup view;

    public TransportControlMediator() {
    }

    @Override
    public void onRegister() {
        // listen for transport changes on the main sequencer
        register(getController(), OnSystemSequencerTransportChange.class,
                new EventObserver<OnSystemSequencerTransportChange>() {
                    @Override
                    public void trigger(OnSystemSequencerTransportChange object) {
                        view.selectPlayPause(getController().getRack().getSystemSequencer()
                                .isPlaying());
                    }
                });
    }

    public Table createTable(IScreen screen) {
        return new Table();
    }

    @Override
    public void onCreate(IScreen screen) {
        final Table table = createTable(screen);
        //table.debug();

        view = new TransportGroup(screen.getSkin());
        view.setOnTransportGroupListener(new OnTransportGroupListener() {
            @Override
            public void onTransposeClick() {
            }

            @Override
            public void onTapClick() {
            }

            @Override
            public void onStopClick() {
                try {
                    getController().execute(ISystemSequencer.COMMAND_STOP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRecordChange(boolean selected) {
            }

            @Override
            public void onPlayChange(boolean selected) {
                if (selected) {
                    try {
                        getController().execute(ISystemSequencer.COMMAND_PLAY,
                                SequencerMode.PATTERN.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getController().execute(ISystemSequencer.COMMAND_STOP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        table.add(view);
    }
}
