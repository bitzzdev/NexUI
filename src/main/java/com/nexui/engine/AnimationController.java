package com.nexui.engine;

import com.nexui.model.AnimationType;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;

/**
 * Frame-rate independent smooth animation interpolator.
 */
public class AnimationController {

    public static Rect2i getAnimatedBounds(UIComponent component, Rect2i baseBounds, float delta) {
        if (component == null || baseBounds == null) return baseBounds;
        float progress = interpolate(component.getStyle().getAnimationType(), delta);
        int w = Math.round(baseBounds.width() * progress);
        int h = Math.round(baseBounds.height() * progress);
        return new Rect2i(baseBounds.x(), baseBounds.y(), Math.max(1, w), Math.max(1, h));
    }

    public static float interpolate(AnimationType type, float progress) {
        float t = Math.clamp(progress, 0f, 1f);
        return switch (type) {
            case NONE -> 1.0f;
            case FADE, SLIDE -> easeOutCubic(t);
            case SCALE -> easeOutBack(t);
            case SPRING -> springInterpolate(t);
            case BOUNCE -> bounceOut(t);
            case ELASTIC -> elasticOut(t);
            case OVERSHOOT -> overshootInterpolate(t);
        };
    }

    private static float easeOutCubic(float x) {
        return 1f - (float) Math.pow(1f - x, 3);
    }

    private static float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1f + c3 * (float) Math.pow(x - 1f, 3) + c1 * (float) Math.pow(x - 1f, 2);
    }

    private static float springInterpolate(float x) {
        return (float) (Math.sin(-13.0 * (x + 1.0) * Math.PI / 2.0) * Math.pow(2.0, -10.0 * x) + 1.0);
    }

    private static float bounceOut(float x) {
        float n1 = 7.5625f;
        float d1 = 2.75f;
        if (x < 1f / d1) {
            return n1 * x * x;
        } else if (x < 2f / d1) {
            return n1 * (x -= 1.5f / d1) * x + 0.75f;
        } else if (x < 2.5f / d1) {
            return n1 * (x -= 2.25f / d1) * x + 0.9375f;
        } else {
            return n1 * (x -= 2.625f / d1) * x + 0.984375f;
        }
    }

    private static float elasticOut(float x) {
        float c4 = (2f * (float) Math.PI) / 3f;
        return x == 0f ? 0f : x == 1f ? 1f : (float) Math.pow(2f, -10f * x) * (float) Math.sin((x * 10f - 0.75f) * c4) + 1f;
    }

    private static float overshootInterpolate(float x) {
        float tension = 2.5f;
        x -= 1.0f;
        return x * x * ((tension + 1.0f) * x + tension) + 1.0f;
    }
}
