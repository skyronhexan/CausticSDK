
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarChangeEvent;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

/**
 * The {@link PaneStack} holds a stack of panes and uses a selectedIndex to show
 * the top pane while hiding all other panes.
 * <p>
 * The PaneStack also carries a {@link ButtonBar} option for quick tab bar like
 * functionality.
 * 
 * @author Michael Schmalle
 */
public class PaneStack extends Table {

    private Skin skin;

    private Stack stack;

    private ButtonBar buttonBar;

    private Table extrasBar;

    public Table getToolsBar() {
        if (extrasBar == null)
            extrasBar = new Table(getSkin());
        return extrasBar;
    }

    private Skin getSkin() {
        return skin;
    }

    Array<Actor> pendingPanes = new Array<Actor>();

    private int buttonBarAlign;

    private Float maxButtonSize;

    public void setMaxButtonSize(Float value) {
        maxButtonSize = value;
        invalidateHierarchy();
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex;

    private Table toolBar;

    private OnPaneStackListener listener;

    private PaneStackStyle style;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        selectedIndex = value;
        if (listener != null)
            listener.onChange(selectedIndex);
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param skin
     * @param buttonBarAlign An {@link Align} value, top or bottom.
     */
    public PaneStack(Skin skin, String styleName, int buttonBarAlign, float maxButtonSize) {
        super(skin);
        style = skin.get(styleName, PaneStackStyle.class);
        this.skin = skin;
        this.buttonBarAlign = buttonBarAlign;
        this.maxButtonSize = maxButtonSize;
        initialize();
    }

    private void initialize() {
        toolBar = new Table(getSkin());
        toolBar.left();

        Array<ButtonBarItem> items = new Array<ButtonBar.ButtonBarItem>();

        buttonBar = new ButtonBar(getSkin(), items, false, style.buttonStyle);
        buttonBar.setMaxButtonSize(maxButtonSize);
        buttonBar.setGap(2f);
        buttonBar.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ButtonBarChangeEvent) {
                    ButtonBarChangeEvent e = (ButtonBarChangeEvent)event;
                    int index = e.getSelectedIndex();
                    setSelectedIndex(index);
                    return true;
                }
                return false;
            }
        });

        if (extrasBar == null)
            extrasBar = new Table(getSkin());

        toolBar.add(buttonBar);
        toolBar.add(extrasBar).fillY();

        stack = new Stack();

        if (buttonBarAlign == Align.top) {
            //add(buttonBar).expandX().height(30f).align(Align.left);
            //add(extrasBar);
            add(toolBar).fillX().padTop(4f).padLeft(4f).padRight(4f).left();
            row();
            add(stack).fill().expand().pad(4f);
        } else {
            add(stack).fill().expand();
            row();
            //add(buttonBar).expandX().height(30f).align(Align.left);
            //add(extrasBar);
            add(toolBar).expandX().fillX();
        }
    }

    protected void updateSelectedIndex() {
        buttonBar.select(selectedIndex, true);
        for (Actor actor : stack.getChildren()) {
            actor.setVisible(false);
        }
        stack.getChildren().get(selectedIndex).setVisible(true);
    }

    @Override
    public void layout() {
        if (pendingPanes.size > 0) {
            Array<ButtonBarItem> labels = new Array<ButtonBarItem>();
            for (Actor pane : pendingPanes) {
                stack.addActor(pane);
                labels.add(new ButtonBarItem(pane.getName(), "", "TODO PaneStack help text"));
            }
            pendingPanes.clear();
            buttonBar.setItems(labels);
        }

        super.layout();

        updateSelectedIndex();
    }

    public void addPane(Actor actor) {
        pendingPanes.add(actor);
    }

    public void setOnOnPaneStackListener(OnPaneStackListener l) {
        listener = l;
    }

    public interface OnPaneStackListener {
        void onChange(int index);
    }

    public static class PaneStackStyle {

        public TextButtonStyle buttonStyle;

        public float maxTabWidth = Float.NaN;

        public PaneStackStyle() {
        }

    }
}
