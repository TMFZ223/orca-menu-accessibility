package org.orcamenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public final class ToggleMonitorAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        if (MenuMonitor.isRunning()) MenuMonitor.stop();
        else MenuMonitor.start();
    }
}
