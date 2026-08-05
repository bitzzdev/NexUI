package com.nexui.engine;

import com.nexui.model.ComponentStyle;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Coordinates Figma-like interactive design mode operations (drag, resize, multi-select, lock, copy-paste style, undo/redo).
 */
public class DesignModeManager {
    private static final DesignModeManager INSTANCE = new DesignModeManager();

    private boolean designModeActive = false;
    private final Set<String> selectedComponentIds = new HashSet<>();
    private String primarySelectedId = null;
    private ComponentStyle clipboardStyle = null;

    private final UndoRedoManager undoRedoManager = new UndoRedoManager();

    private DesignModeManager() {}

    public static DesignModeManager getInstance() {
        return INSTANCE;
    }

    public boolean isDesignModeActive() {
        return designModeActive;
    }

    public void setDesignModeActive(boolean active) {
        this.designModeActive = active;
        if (!active) {
            clearSelection();
        }
    }

    public void toggleDesignMode() {
        setDesignModeActive(!designModeActive);
    }

    public void selectComponent(String id, boolean multiSelect) {
        if (!multiSelect) {
            selectedComponentIds.clear();
        }
        if (id != null) {
            selectedComponentIds.add(id);
            primarySelectedId = id;
        } else if (!multiSelect) {
            primarySelectedId = null;
        }
    }

    public void clearSelection() {
        selectedComponentIds.clear();
        primarySelectedId = null;
    }

    public UIComponent getPrimarySelectedComponent() {
        if (primarySelectedId == null) return null;
        return ProfileRegistry.getInstance().getActiveProfile().getComponent(primarySelectedId);
    }

    public List<UIComponent> getSelectedComponents() {
        List<UIComponent> result = new ArrayList<>();
        LayoutProfile active = ProfileRegistry.getInstance().getActiveProfile();
        for (String id : selectedComponentIds) {
            UIComponent comp = active.getComponent(id);
            if (comp != null) {
                result.add(comp);
            }
        }
        return result;
    }

    public void beginSelectedComponentDrag() {
        undoRedoManager.pushState(ProfileRegistry.getInstance().getActiveProfile());
    }

    public void moveSelectedComponentsTo(Map<String, Rect2i> targetBounds) {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        for (Map.Entry<String, Rect2i> entry : targetBounds.entrySet()) {
            UIComponent comp = profile.getComponent(entry.getKey());
            if (comp != null && !comp.isLocked()) {
                comp.setCurrentBounds(entry.getValue());
            }
        }
    }

    public void moveSelectedComponents(int dx, int dy) {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        undoRedoManager.pushState(profile);

        for (UIComponent comp : getSelectedComponents()) {
            if (comp.isLocked()) continue;
            Rect2i cur = comp.getCurrentBounds();
            int newX = cur.x() + dx;
            int newY = cur.y() + dy;
            if (profile.isGridSnapEnabled()) {
                newX = GridSnapEngine.snapValue(newX, profile.getGridSize());
                newY = GridSnapEngine.snapValue(newY, profile.getGridSize());
            }
            comp.setCurrentBounds(cur.translate(newX - cur.x(), newY - cur.y()));
        }
    }

    public void resizeSelectedComponent(int newWidth, int newHeight) {
        UIComponent primary = getPrimarySelectedComponent();
        if (primary == null || primary.isLocked()) return;
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        undoRedoManager.pushState(profile);

        Rect2i cur = primary.getCurrentBounds();
        if (profile.isGridSnapEnabled()) {
            newWidth = GridSnapEngine.snapValue(newWidth, profile.getGridSize());
            newHeight = GridSnapEngine.snapValue(newHeight, profile.getGridSize());
        }
        primary.setCurrentBounds(cur.withSize(newWidth, newHeight));
    }

    public void toggleLockSelected() {
        UIComponent primary = getPrimarySelectedComponent();
        if (primary != null) {
            primary.setLocked(!primary.isLocked());
        }
    }

    public void copyStyle() {
        UIComponent primary = getPrimarySelectedComponent();
        if (primary != null) {
            clipboardStyle = primary.getStyle().copy();
        }
    }

    public void pasteStyle() {
        if (clipboardStyle == null) return;
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        undoRedoManager.pushState(profile);

        for (UIComponent comp : getSelectedComponents()) {
            if (!comp.isLocked()) {
                comp.setStyle(clipboardStyle.copy());
            }
        }
    }

    public void resetSelectedComponent() {
        UIComponent primary = getPrimarySelectedComponent();
        if (primary != null) {
            undoRedoManager.pushState(ProfileRegistry.getInstance().getActiveProfile());
            primary.reset();
        }
    }

    public void resetLayout() {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        undoRedoManager.pushState(profile);

        for (UIComponent comp : profile.getComponents().values()) {
            comp.reset();
        }
    }

    public void undo() {
        LayoutProfile undone = undoRedoManager.undo(ProfileRegistry.getInstance().getActiveProfile());
        ProfileRegistry.getInstance().registerProfile(undone);
    }

    public void redo() {
        LayoutProfile redone = undoRedoManager.redo(ProfileRegistry.getInstance().getActiveProfile());
        ProfileRegistry.getInstance().registerProfile(redone);
    }
}
