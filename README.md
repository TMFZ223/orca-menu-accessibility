# Orca Menu Diagnostics 0.1.0

Experimental, read-only diagnostic plugin for IntelliJ IDEA / PyCharm 2026.2 on Linux X11.

## Build

Prerequisites: JDK 21, Gradle 9.x, internet access to Gradle Plugin Portal and JetBrains repositories.

```bash
gradle buildPlugin
```

The installable plugin ZIP is generated in `build/distributions/`.
This repository does not yet include a Gradle wrapper or a prebuilt plugin binary.

## Usage

1. Enable the IDE's built-in screen reader support and restart the IDE.
2. Install the built plugin ZIP via Settings > Plugins > Install Plugin from Disk.
3. Restart the IDE and open a project. The monitor starts automatically.
4. Open an editor context menu, navigate with arrow keys, then open the hamburger menu and submenus.
5. Use Help > Show Log in Files and search `idea.log` for `[OrcaMenu]`.
6. Tools > Toggle Orca Menu Diagnostics stops or restarts the monitor.

## Interpretation

- `selected=...` means Swing MenuSelectionManager saw a selected JMenuItem.
- `selection cleared` or no selection messages during navigation may mean JetBrains uses a custom popup implementation or selection isn't routed through Swing MenuSelectionManager.
- Even when accessible names are present, this does **not** prove that AT-SPI2/Orca receives the corresponding accessibility events.
- `window=...` is supporting evidence only; many popups do not create separate AWT windows.

## Privacy

Logs may contain menu item labels, project names, file paths or other sensitive data. Review and redact before sharing.

## Limitations

No AT-SPI2 inspection, synthetic focus events, screen reader speech, or internal JetBrains popup API integration yet. This is a diagnostic experiment, not a verified fix.
