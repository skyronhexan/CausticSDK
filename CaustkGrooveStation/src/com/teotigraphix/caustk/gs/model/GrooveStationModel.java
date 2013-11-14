
package com.teotigraphix.caustk.gs.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveStation;
import com.teotigraphix.libgdx.model.CaustkModelBase;

@Singleton
public class GrooveStationModel extends CaustkModelBase implements IGrooveStationModel {

    private GrooveStation grooveStation;

    @Override
    public void setGrooveStation(GrooveStation grooveStation) {
        this.grooveStation = grooveStation;
    }

    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        selectedIndex = value;
    }

    public GrooveStationModel() {
    }

    @Override
    public GrooveBox getSelectedGrooveBox() {
        return grooveStation.getGrooveBox(selectedIndex);
    }

}
