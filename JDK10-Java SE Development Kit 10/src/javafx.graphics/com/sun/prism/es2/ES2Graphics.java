/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.prism.es2;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RenderTarget;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public class ES2Graphics extends BaseShaderGraphics {

    private final ES2Context context;

    private ES2Graphics(ES2Context context, RenderTarget target) {
        super(context, target);
        this.context = context;
    }

    static ES2Graphics create(ES2Context context, RenderTarget target) {
        if (target == null) {
            return null;
        }
        return new ES2Graphics(context, target);
    }

    static void clearBuffers(ES2Context context, Color color, boolean clearColor,
            boolean clearDepth, boolean ignoreScissor) {
        context.getGLContext().clearBuffers(color, clearColor, clearDepth,
                ignoreScissor);

    }

    public void clearQuad(float x1, float y1, float x2, float y2) {
        // note that unlike clear(), this method does not currently
        // attempt to clear the depth buffer...
        context.setRenderTarget(this);
        context.flushVertexBuffer();
        CompositeMode mode = getCompositeMode();
        // set the blend mode to CLEAR
        context.updateCompositeMode(CompositeMode.CLEAR);
        Paint oldPaint = getPaint();
        setPaint(Color.BLACK); // any color will do...
        fillQuad(x1, y1, x2, y2);
        context.flushVertexBuffer();
        setPaint(oldPaint);
        // restore default blend mode
        context.updateCompositeMode(mode);
    }

    public void clear(Color color) {
        context.validateClearOp(this);
        this.getRenderTarget().setOpaque(color.isOpaque());
        clearBuffers(context, color, true, isDepthBuffer(), false);

    }

    public void sync() {
        context.flushVertexBuffer();
        context.getGLContext().finish();
    }

    /**
     * Called from ES2SwapChain to force the render target to be revalidated
     * (context made current, viewport and projection matrix updated, etc)
     * in response to a window resize event.
     */
    void forceRenderTarget() {
        context.forceRenderTarget(this);
    }

    @Override
    public void transform(BaseTransform transform) {
        // Treat transform as identity matrix if platform doesn't support 3D
        // and transform isn't a 2D matrix
        if (!GraphicsPipeline.getPipeline().is3DSupported()
                && !transform.is2D()) {
            return;
        }
        super.transform(transform);
    }

    @Override
    public void translate(float tx, float ty, float tz) {
        // Treat translate as identity translate if platform doesn't support 3D
        // and it isn't a 2D translate
        if (!GraphicsPipeline.getPipeline().is3DSupported() &&  tz != 0.0f) {
            return;
        }
        super.translate(tx, ty, tz);
    }

    @Override
    public void scale(float sx, float sy, float sz) {
        // Treat scale as identity scale if platform doesn't support 3D
        // and it isn't a 2D scale
        if (!GraphicsPipeline.getPipeline().is3DSupported() &&  sz != 1.0f) {
            return;
        }
        super.scale(sx, sy, sz);
    }

    @Override
    public void setCamera(NGCamera camera) {
        // Use the default ParallelCamera if platform doesn't support 3D
        if (GraphicsPipeline.getPipeline().is3DSupported()) {
            super.setCamera(camera);
        }
    }
}
