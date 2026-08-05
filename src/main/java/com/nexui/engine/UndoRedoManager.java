package com.nexui.engine;

import com.nexui.model.LayoutProfile;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages Undo/Redo history stack for design studio layout edits.
 */
public class UndoRedoManager {
    private static final int MAX_HISTORY = 50;
    private final Deque<LayoutProfile> undoStack = new ArrayDeque<>();
    private final Deque<LayoutProfile> redoStack = new ArrayDeque<>();

    public void pushState(LayoutProfile currentProfile) {
        if (currentProfile == null) return;
        if (undoStack.size() >= MAX_HISTORY) {
            undoStack.removeLast();
        }
        undoStack.push(currentProfile.copy(currentProfile.getId(), currentProfile.getName()));
        redoStack.clear();
    }

    public LayoutProfile undo(LayoutProfile currentState) {
        if (canUndo()) {
            redoStack.push(currentState.copy(currentState.getId(), currentState.getName()));
            return undoStack.pop();
        }
        return currentState;
    }

    public LayoutProfile redo(LayoutProfile currentState) {
        if (canRedo()) {
            undoStack.push(currentState.copy(currentState.getId(), currentState.getName()));
            return redoStack.pop();
        }
        return currentState;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
