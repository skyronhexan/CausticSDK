////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.workstation;

import java.util.UUID;

public class SongSetFactory extends CaustkSubFactoryBase {

    public SongSetFactory() {
    }

    public SongBank createSongBank(ComponentInfo info, UUID patternBankId) {
        SongBank songBank = new SongBank(info, patternBankId);
        return songBank;
    }

    public SongBank createSongBank(ComponentInfo info, PatternBank patternBank) {
        SongBank songBank = new SongBank(info, patternBank);
        return songBank;
    }

    public Song createSong(ComponentInfo info, SongBank songBank) {
        Song song = new Song(info, songBank);
        return song;
    }
}