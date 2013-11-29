
package com.teotigraphix.caustic.meta.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.teotigraphix.caustic.meta.AndroidExplorer;
import com.teotigraphix.caustic.meta.MainActivity;
import com.teotigraphix.caustic.meta.R;
import com.teotigraphix.caustic.meta.model.FileModel;
import com.teotigraphix.caustic.meta.model.FileModel.OnFileModelChangeListener;
import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class MainActivityMediator {

    private MainActivity activity;

    FileModel fileModel;

    private TextView causticFileText;

    private EditText artistText;

    private EditText titleText;

    private EditText descriptionText;

    private EditText linkText;

    private EditText linkURLText;

    private Button addButton;

    private Button removeButton;

    private ToggleButton playPauseButton;

    public MainActivityMediator(MainActivity activity, FileModel fileModel) {
        this.activity = activity;
        this.fileModel = fileModel;
    }

    public void onAttach() {
        fileModel.setOnFileModelChangeListener(new OnFileModelChangeListener() {
            @Override
            public void onFileChange(CausticFile causticFile) {
                loadCausticSongFile();
                refreshView();
            }
        });
    }

    private List<EditText> formItems = new ArrayList<EditText>();

    public void onCreate() {
        causticFileText = (TextView)activity.findViewById(R.id.causticFileText);
        artistText = (EditText)activity.findViewById(R.id.artistText);
        titleText = (EditText)activity.findViewById(R.id.titleText);
        descriptionText = (EditText)activity.findViewById(R.id.descriptionText);
        descriptionText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (((EditText)v).getLineCount() >= 8)
                        return true;
                }
                return false;
            }
        });

        linkText = (EditText)activity.findViewById(R.id.linkText);
        linkURLText = (EditText)activity.findViewById(R.id.linkURLText);

        formItems.add(artistText);
        formItems.add(titleText);
        formItems.add(descriptionText);
        formItems.add(linkText);
        formItems.add(linkURLText);

        Button browseButton = (Button)activity.findViewById(R.id.browseButton);
        browseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, AndroidExplorer.class);
                i.putExtra(AndroidExplorer.EXTRA_ROOT, RuntimeUtils.getCausticSongsDirectory());
                activity.startActivityForResult(i, 1);
            }
        });

        playPauseButton = (ToggleButton)activity.findViewById(R.id.playButton);
        playPauseButton.setEnabled(false);
        playPauseButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if the button is now checked, play (1),  otherwise stop (0)
                OutputPanelMessage.PLAY.send(activity.getGenerator(), isChecked ? 1 : 0);
            }
        });

        addButton = (Button)activity.findViewById(R.id.addButton);
        addButton.setEnabled(false);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    saveCausticFile();
                }
            }
        });

        removeButton = (Button)activity.findViewById(R.id.removeButton);
        removeButton.setEnabled(false);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveSongAs(fileModel.getCausticFile().getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                refreshView();

                //                try {
                //                    getController().getRackSet().clearMachines();
                //                } catch (CausticException e) {
                //                    e.printStackTrace();
                //                }
            }
        });
    }

    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(activity.getGenerator(), name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        FileUtils.copyFileToDirectory(song, file.getParentFile());
        song.delete();
        return file;
    }

    protected boolean isValid() {
        boolean valid = true;
        String formItemName = null;
        View formItem = null;
        for (EditText editText : formItems) {
            if (!checkText(editText)) {
                formItem = editText;
                formItemName = (String)formItem.getTag();
                break;
            }
        }

        if (formItem != null) {
            Toast.makeText(activity, "'" + formItemName + "' must be filled in", Toast.LENGTH_SHORT)
                    .show();
            formItem.requestFocus();
        }

        return valid;
    }

    protected void saveCausticFile() {
        if (!isValid())
            return;

        CausticFile causticFile = fileModel.getCausticFile();

        causticFile.setArtist(artistText.getText().toString());
        causticFile.setTitle(titleText.getText().toString());
        causticFile.setDescription(descriptionText.getText().toString());
        causticFile.setLinkText(linkText.getText().toString());
        causticFile.setLinkUrl(linkURLText.getText().toString());

        //if (causticFile.hasMetadata()) {
        // replacing data, need to resave file until I figure out
        // how to chop bytes

        saveSong(causticFile.getFile().getName().replace(".caustic", ""));

        //}

        try {
            causticFile.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkText(EditText editText) {
        if (editText.getText().length() == 0) {
            return false;
        }
        return true;
    }

    private void loadCausticSongFile() {
        CausticFile causticFile = fileModel.getCausticFile();
        RackMessage.LOAD_SONG
                .send(activity.getGenerator(), causticFile.getFile().getAbsolutePath());
    }

    /**
     * Needs to refresh the view based on the presence of a CausticFile.
     */
    private void refreshView() {
        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        playPauseButton.setEnabled(false);
        playPauseButton.setChecked(false);

        CausticFile causticFile = fileModel.getCausticFile();
        if (causticFile.isLoaded()) {
            addButton.setEnabled(true);
            playPauseButton.setEnabled(true);

            causticFileText.setText(causticFile.getFile().getAbsolutePath());
            artistText.setText(causticFile.getArtist());
            titleText.setText(causticFile.getTitle());
            descriptionText.setText(causticFile.getDescription());
            linkText.setText(causticFile.getLinkText());
            linkURLText.setText(causticFile.getLinkUrl());
        } else {
            causticFileText.setText("");
            artistText.setText("");
            titleText.setText("");
            descriptionText.setText("");
            linkText.setText("");
            linkURLText.setText("");
        }

        if (causticFile.hasMetadata()) {
            removeButton.setEnabled(true);
        }
    }
}