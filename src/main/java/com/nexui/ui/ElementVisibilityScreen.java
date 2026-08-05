package com.nexui.ui;

import com.nexui.api.WidgetCategory;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen for toggling the relocator visibility of every UI element placement in the
 * active profile. Hiding only removes the relocator box from the design canvas;
 * it never hides the real in-game element. Category groups (e.g. the hotbar) can be
 * toggled as a single option.
 */
public class ElementVisibilityScreen extends Screen {
    private static final int PANEL_X = 40;
    private static final int PANEL_Y = 80;
    private static final int ROW_HEIGHT = 28;
    private static final int ROW_GAP = 4;
    private static final int ROW_STEP = ROW_HEIGHT + ROW_GAP;
    private static final int PILL_WIDTH = 64;
    private static final int PILL_HEIGHT = 20;
    private static final int FOOTER_BUTTON_WIDTH = 150;
    private static final int FOOTER_BUTTON_HEIGHT = 34;
    private static final int FOOTER_BUTTON_GAP = 16;

    private final List<UIComponent> components = new ArrayList<>();
    private int scrollOffset = 0;

    public ElementVisibilityScreen() {
        super(Component.literal("NexUI Element Visibility"));
        LayoutProfile active = ProfileRegistry.getInstance().getActiveProfile();
        if (active != null) {
            this.components.addAll(active.getComponents().values());
        }
    }

    private int panelWidth() {
        return width - PANEL_X * 2;
    }

    private int panelHeight() {
        return height - 170;
    }

    private List<WidgetCategory> groupOrder() {
        List<WidgetCategory> groups = new ArrayList<>();
        for (UIComponent comp : components) {
            WidgetCategory cat = comp.getCategory();
            if (!groups.contains(cat)) {
                groups.add(cat);
            }
        }
        return groups;
    }

    private int groupRows() {
        return groupOrder().size();
    }

    private int groupSectionHeight() {
        return groupRows() * ROW_STEP + 8;
    }

    private int maxScroll() {
        int individualArea = panelHeight() - groupSectionHeight();
        return Math.max(0, components.size() * ROW_STEP - individualArea + 8);
    }

    private Rect2i footerButtonBounds(int index) {
        int total = FOOTER_BUTTON_WIDTH * 3 + FOOTER_BUTTON_GAP * 2;
        int startX = (width - total) / 2;
        int y = height - FOOTER_BUTTON_HEIGHT - 18;
        return new Rect2i(startX + index * (FOOTER_BUTTON_WIDTH + FOOTER_BUTTON_GAP), y, FOOTER_BUTTON_WIDTH, FOOTER_BUTTON_HEIGHT);
    }

    private int visibleCount(WidgetCategory category) {
        int count = 0;
        for (UIComponent comp : components) {
            if (comp.getCategory() == category && comp.isVisible()) count++;
        }
        return count;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        // Background/blur is already extracted by Screen.extractRenderStateWithTooltipAndSubtitles.
        super.extractRenderState(context, mouseX, mouseY, delta);

        int visible = 0;
        for (UIComponent comp : components) {
            if (comp.isVisible()) visible++;
        }

        context.text(this.font, "NexUI Element Visibility", PANEL_X, 40, ColorRGBA.ACCENT_CYAN.toARGB(), false);
        context.text(this.font, "Click a row to show or hide that element's relocator on the design canvas. The real in-game UI is never hidden.", PANEL_X, 56, 0xAAFFFFFF, false);
        String counter = visible + " / " + components.size() + " visible";
        context.text(this.font, counter, width - PANEL_X - this.font.width(counter), 40, 0xAAFFFFFF, false);

        // List panel background
        ComponentStyle panelStyle = new ComponentStyle();
        panelStyle.setBackgroundColor(new ColorRGBA(12, 12, 18, 220));
        panelStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        panelStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, new Rect2i(PANEL_X, PANEL_Y, panelWidth(), panelHeight()), panelStyle);

        // Scroll-clipped rows
        context.enableScissor(PANEL_X + 2, PANEL_Y + 2, PANEL_X + panelWidth() - 2, PANEL_Y + panelHeight() - 2);

        // Category group rows (always visible, not affected by scroll)
        List<WidgetCategory> groups = groupOrder();
        int groupY = PANEL_Y + 6;
        for (WidgetCategory category : groups) {
            renderGroupRow(context, category, new Rect2i(PANEL_X + 6, groupY, panelWidth() - 12, ROW_HEIGHT));
            groupY += ROW_STEP;
        }

        // Individual component rows (scrollable)
        for (int i = 0; i < components.size(); i++) {
            int ry = groupY + 4 + i * ROW_STEP - scrollOffset;
            if (ry + ROW_HEIGHT < PANEL_Y || ry > PANEL_Y + panelHeight()) continue;
            renderRow(context, components.get(i), new Rect2i(PANEL_X + 6, ry, panelWidth() - 12, ROW_HEIGHT));
        }
        context.disableScissor();

        // Footer buttons
        renderFooterButton(context, footerButtonBounds(0), "Show All", false);
        renderFooterButton(context, footerButtonBounds(1), "Hide All", false);
        renderFooterButton(context, footerButtonBounds(2), "Done", true);
    }

    private void renderGroupRow(GuiGraphicsExtractor context, WidgetCategory category, Rect2i rowBounds) {
        int count = 0;
        for (UIComponent comp : components) {
            if (comp.getCategory() == category) count++;
        }
        int visible = visibleCount(category);
        boolean allVisible = visible == count;

        ComponentStyle rowStyle = new ComponentStyle();
        rowStyle.setBackgroundColor(allVisible ? new ColorRGBA(30, 60, 70, 220) : new ColorRGBA(24, 24, 32, 200));
        rowStyle.setBorderColor(new ColorRGBA(99, 102, 241, 200));
        rowStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, rowBounds, rowStyle);

        context.text(this.font, category.getLabel() + " (Group)", rowBounds.x() + 10, rowBounds.y() + 6, allVisible ? ColorRGBA.WHITE.toARGB() : 0x88FFFFFF, false);

        int pillX = rowBounds.right() - PILL_WIDTH - 8;
        int pillY = rowBounds.y() + (ROW_HEIGHT - PILL_HEIGHT) / 2;
        ComponentStyle pillStyle = new ComponentStyle();
        pillStyle.setBackgroundColor(allVisible ? new ColorRGBA(6, 182, 212, 220) : new ColorRGBA(60, 60, 70, 200));
        pillStyle.setBorderWidth(0);
        RenderPipeline.renderStyledPanel(context, new Rect2i(pillX, pillY, PILL_WIDTH, PILL_HEIGHT), pillStyle);
        String label = visible + "/" + count;
        context.text(this.font, label, pillX + (PILL_WIDTH - this.font.width(label)) / 2, pillY + 5, allVisible ? ColorRGBA.BLACK.toARGB() : ColorRGBA.WHITE.toARGB(), false);
    }

    private void renderRow(GuiGraphicsExtractor context, UIComponent comp, Rect2i rowBounds) {
        ComponentStyle rowStyle = new ComponentStyle();
        rowStyle.setBackgroundColor(comp.isVisible() ? new ColorRGBA(30, 41, 59, 220) : new ColorRGBA(24, 24, 32, 200));
        rowStyle.setBorderColor(comp.isVisible() ? new ColorRGBA(6, 182, 212, 160) : new ColorRGBA(99, 102, 241, 80));
        rowStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, rowBounds, rowStyle);

        context.text(this.font, comp.getName(), rowBounds.x() + 10, rowBounds.y() + 6, comp.isVisible() ? ColorRGBA.WHITE.toARGB() : 0x88FFFFFF, false);

        String category = comp.getCategory().getLabel();
        int categoryX = rowBounds.right() - PILL_WIDTH - 16 - this.font.width(category);
        context.text(this.font, category, categoryX, rowBounds.y() + 6, 0x88FFFFFF, false);

        // Toggle pill
        int pillX = rowBounds.right() - PILL_WIDTH - 8;
        int pillY = rowBounds.y() + (ROW_HEIGHT - PILL_HEIGHT) / 2;
        ComponentStyle pillStyle = new ComponentStyle();
        pillStyle.setBackgroundColor(comp.isVisible() ? new ColorRGBA(6, 182, 212, 220) : new ColorRGBA(60, 60, 70, 200));
        pillStyle.setBorderWidth(0);
        RenderPipeline.renderStyledPanel(context, new Rect2i(pillX, pillY, PILL_WIDTH, PILL_HEIGHT), pillStyle);
        String label = comp.isVisible() ? "ON" : "OFF";
        context.text(this.font, label, pillX + (PILL_WIDTH - this.font.width(label)) / 2, pillY + 5, comp.isVisible() ? ColorRGBA.BLACK.toARGB() : ColorRGBA.WHITE.toARGB(), false);
    }

    private void renderFooterButton(GuiGraphicsExtractor context, Rect2i bounds, String label, boolean primary) {
        ComponentStyle buttonStyle = new ComponentStyle();
        buttonStyle.setBackgroundColor(primary ? new ColorRGBA(99, 102, 241, 230) : new ColorRGBA(30, 30, 40, 220));
        buttonStyle.setBorderColor(primary ? ColorRGBA.ACCENT_BLUE : new ColorRGBA(99, 102, 241, 120));
        buttonStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, bounds, buttonStyle);
        context.text(this.font, label, bounds.x() + (bounds.width() - this.font.width(label)) / 2, bounds.y() + (bounds.height() - 8) / 2, primary ? ColorRGBA.WHITE.toARGB() : 0xAAFFFFFF, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClicked) {
        int mx = (int) event.x();
        int my = (int) event.y();

        if (footerButtonBounds(0).contains(mx, my)) {
            setAllVisible(true);
            return true;
        }
        if (footerButtonBounds(1).contains(mx, my)) {
            setAllVisible(false);
            return true;
        }
        if (footerButtonBounds(2).contains(mx, my)) {
            closeToDesignMode();
            return true;
        }

        // Category group rows
        List<WidgetCategory> groups = groupOrder();
        int groupY = PANEL_Y + 6;
        for (WidgetCategory category : groups) {
            Rect2i rowBounds = new Rect2i(PANEL_X + 6, groupY, panelWidth() - 12, ROW_HEIGHT);
            if (rowBounds.contains(mx, my)) {
                toggleGroup(category);
                return true;
            }
            groupY += ROW_STEP;
        }

        // Individual rows
        int panelH = panelHeight();
        for (int i = 0; i < components.size(); i++) {
            int ry = groupY + 4 + i * ROW_STEP - scrollOffset;
            if (ry + ROW_HEIGHT < PANEL_Y || ry > PANEL_Y + panelH) continue;
            Rect2i rowBounds = new Rect2i(PANEL_X + 6, ry, panelWidth() - 12, ROW_HEIGHT);
            if (rowBounds.contains(mx, my)) {
                UIComponent comp = components.get(i);
                comp.setVisible(!comp.isVisible());
                return true;
            }
        }

        return super.mouseClicked(event, doubleClicked);
    }

    private void toggleGroup(WidgetCategory category) {
        int count = 0;
        for (UIComponent comp : components) {
            if (comp.getCategory() == category) count++;
        }
        boolean allVisible = visibleCount(category) == count;
        for (UIComponent comp : components) {
            if (comp.getCategory() == category) {
                comp.setVisible(!allVisible);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.clamp(scrollOffset - (int) Math.round(verticalAmount * 24.0), 0, maxScroll());
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            closeToDesignMode();
            return true;
        }
        return super.keyPressed(event);
    }

    private void setAllVisible(boolean visible) {
        for (UIComponent comp : components) {
            comp.setVisible(visible);
        }
    }

    private void closeToDesignMode() {
        Minecraft.getInstance().gui.setScreen(new DesignModeScreen());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
