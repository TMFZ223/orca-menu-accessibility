package org.orcamenu;

import com.intellij.openapi.diagnostic.Logger;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.util.Arrays;
import java.util.Objects;

/** Read-only diagnostics. Does not synthesize focus or accessibility events. */
public final class MenuMonitor {
    private static final Logger LOG = Logger.getInstance(MenuMonitor.class);
    private static final MenuSelectionManager MANAGER = MenuSelectionManager.defaultManager();
    private static final ChangeListener SELECTION = event -> inspectSelection();
    private static final AWTEventListener WINDOWS = event -> {
        if (event instanceof java.awt.event.WindowEvent we) {
            int id = we.getID();
            if (id == java.awt.event.WindowEvent.WINDOW_OPENED ||
                id == java.awt.event.WindowEvent.WINDOW_ACTIVATED) {
                Window window = we.getWindow();
                LOG.info("[OrcaMenu] window=" + window.getClass().getName() +
                    " name=" + window.getName());
            }
        }
    };
    private static boolean running;
    private static MenuElement last;

    private MenuMonitor() {}

    public static synchronized boolean isRunning() { return running; }

    public static synchronized void start() {
        if (running) return;
        running = true;
        MANAGER.addChangeListener(SELECTION);
        Toolkit.getDefaultToolkit().addAWTEventListener(WINDOWS, AWTEvent.WINDOW_EVENT_MASK);
        LOG.info("[OrcaMenu] STARTED: Swing selection and window monitoring");
        inspectSelection();
    }

    public static synchronized void stop() {
        if (!running) return;
        MANAGER.removeChangeListener(SELECTION);
        Toolkit.getDefaultToolkit().removeAWTEventListener(WINDOWS);
        last = null;
        running = false;
        LOG.info("[OrcaMenu] STOPPED");
    }

    private static void inspectSelection() {
        MenuElement[] path = MANAGER.getSelectedPath();
        MenuElement selected = Arrays.stream(path)
            .filter(e -> e instanceof JMenuItem)
            .reduce((a, b) -> b).orElse(null);
        if (selected == last) return;
        last = selected;
        if (selected == null) {
            LOG.info("[OrcaMenu] selection cleared; pathLength=" + path.length);
            return;
        }
        JMenuItem item = (JMenuItem) selected;
        AccessibleContext context = item.getAccessibleContext();
        String name = context == null ? null : context.getAccessibleName();
        if (name == null || name.isBlank()) name = item.getText();
        LOG.info("[OrcaMenu] selected=" + Objects.toString(name, "<unnamed>") +
            " class=" + item.getClass().getName() +
            " role=" + (context == null ? "<null>" : context.getAccessibleRole()) +
            " states=" + (context == null ? "<null>" : context.getAccessibleStateSet()) +
            " enabled=" + item.isEnabled() +
            " submenu=" + (item instanceof JMenu) +
            " pathLength=" + path.length);
    }
}
