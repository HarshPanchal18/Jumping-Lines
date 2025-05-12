# Jumping Lines - Smoothly Navigate Through Your Code with Ease

<img align="right" src="src/main/resources/META-INF/pluginIcon.svg" style ="height:150px;width:150px;padding:8px;" alt="Plugin Logo">

<a href="https://plugins.jetbrains.com/plugin/22878-jumping-lines"><img src="https://img.shields.io/website?down_color=red&down_message=Under%20work&label=Plugin&logo=jetbrains&style=for-the-badge&up_color=blue&up_message=Stable&url=https://plugins.jetbrains.com/plugin/22878-jumping-lines"></a>
![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/22878?style=for-the-badge&logo=jetbrains&label=Total%20Downloads&color=blue)
![JetBrains Plugin Rating](https://img.shields.io/jetbrains/plugin/r/rating/22878?style=for-the-badge&logo=jetbrains&label=Rating&color=blue)
![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/22878?style=for-the-badge&logo=jetbrains&label=Version&color=blue)

### `Introduction:`

**Jumping Lines** is a powerful plugin for JetBrains IDEs that allows you to easily navigate through your code by
jumping multiple lines forward and backward with fewer keystrokes than ever.

With this plugin, you can easily switch between different parts of your code, select multiple lines in most easier way,
reducing the need to scroll through long files or navigate through complex codebases.

[Preview](https://github.com/HarshPanchal18/Jumping-Lines/assets/63415584/fe86d298-7875-4744-ba4b-68e21a876d70)

### `Key Features:`

1. **Line Jumping:** Jump forward or backward multiple lines at a time.
2. **Line Jumping with selection:** Jump forward or backward multiple lines at a time with selection.
3. **Customizable number of Lines:** Define the number of lines to jump forward and backward through the plugin
   setting.
4. **Jump on Middle line:** Jump on middle line of an editor.
5. **Jump Score:** A score that counts the number of lines you've jumped.
6. **Status bar Widget:** View Jump Score directly from status bar.
7. **Compatible with All JetBrains IDEs:** This plugin is compatible with all JetBrains IDEs, including IntelliJ IDEA,
   PyCharm, Android Studio, PHPStorm, and RubyMine.

### `How to Use:`

1. **Install plugin:** [Download](https://plugins.jetbrains.com/plugin/22878-jumping-lines) and install plugin
   from the JetBrains plugin repository or Search "`Jumping Lines`" through `Settings -> Plugins` in IDE.


2. **Configure plugin via any of following ways:**

> * Open IDE settings and navigate to the `Editor -> Jumping Lines` section, or
>* Go through `Tools Menu -> Jumping Lines`, or
>* Simply press <kbd>Alt + L</kbd>.

3. **Start navigating through following keymaps:**

> * <kbd>Ctrl + Shift + Period</kbd> to make jump `forward`.
>* <kbd>Ctrl + Shift + Comma</kbd> to make jump `backward`.
>* <kbd>Alt + Shift + Period</kbd> to make jump `forward with selection`.
>* <kbd>Alt + Shift + Comma</kbd> to make jump `backward with selection`.
>* <kbd>Ctrl + Comma</kbd> to jump on the `middle line of an editor`.
>* <kbd>Alt + L</kbd> to open `plugin settings`.

### `Benefits:`

1. **Time-Saving:** With Jumping Lines, you can quickly and easily navigate through your code without having to scroll
   through long files or navigate through complex codebases.
2. **Enhanced Productivity:** By reducing the amount of time spent navigating through code, you can focus on writing
   code and solving problems, leading to enhanced productivity.
3. **Customizable:** The plugin allows you to define the number of lines to jump forward and backward, giving you
   complete control over your navigation experience.

### `Conclusion:`

Jumping Lines is a powerful and customizable plugin that simplifies the process of navigating through your code. With
its intuitive interface and efficient performance, you can save time and increase your productivity, making it an
essential tool for any developer. Try it out today and see the difference it can make in your coding experience!

---

# Popularity 😎

<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120" alt="Perplexity AI"/>

## How can I jump multiple lines at once in JetBrains IDEs easily?

To jump multiple lines at once in JetBrains IDEs (like IntelliJ IDEA, PyCharm, etc.), you have a few options:

**1. Use a Plugin (Recommended for Custom Line Jumps)**

- The "Jumping Lines" plugin lets you jump forward or backward by a customizable number of lines.
- Default shortcuts:
    - Jump forward: Ctrl + Shift + Period
    - Jump backward: Ctrl + Shift + Comma
    - Jump forward with selection: Alt + Shift + Period
    - Jump backward with selection: Alt + Shift + Comma
- You can configure how many lines to jump in the plugin settings.

**2. Macros or Plugins for Custom Jumps**

- You can record a macro of several up/down actions and assign it to a shortcut, but this is less smooth than using a
  dedicated plugin.

**3. Vim Plugin**

- If you enable the IdeaVim plugin, you can use Vim-style navigation (e.g., 10j to jump 10 lines down).

**4. Built-in Multiple Carets (for Editing, Not Navigation)**

- For editing multiple lines at once, you can use multiple carets:
    - Windows/Linux: Alt + Shift + Click to place carets, or Alt + Drag to create a vertical caret column.
    - macOS: Option + Shift + Click or Option + Drag.
- Not intended for pure navigation, but useful for editing several lines simultaneously.

**Summary Table**

| Method               | Shortcut Example        | Customizable? | Notes                            |
|:---------------------|:------------------------|:--------------|:---------------------------------|
| Jumping Lines Plugin | Ctrl+Shift+Period/Comma | Yes           | Best for jumping N lines at once |
| Macro                | User-defined            | Yes           | Can be clunky, not always smooth |
| IdeaVim Plugin       | 10j / 10k               | Yes           | Vim-style, requires plugin       |
| Multiple Carets      | Alt+Shift+Click/Drag    | No            | For editing, not pure jumping    |

For most users, installing the "Jumping Lines" plugin is the easiest and most flexible way to jump multiple lines at
once in JetBrains IDEs.

<div style="text-align: center">⁂ References</div>

1. https://plugins.jetbrains.com/plugin/22878-jumping-lines
2. https://www.jetbrains.com/help/idea/multicursor.html
3. https://stackoverflow.com/questions/56631179/how-to-shift-multiple-lines-of-code-in-intellij-by-a-single-space
4. https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000507430-How-to-jump-move-the-cursor-Up-or-Down-by-several-4-5-lines-in-Intellij
5. https://github.com/HarshPanchal18/Jumping-Lines
6. https://foojay.io/today/working-with-multiple-carets-in-intellij-idea/
7. https://stackoverflow.com/questions/1262737/intellij-idea-way-of-editing-multiple-lines
8. https://www.youtube.com/watch?v=l0ii-Oo0XX8
9. https://www.reddit.com/r/Jetbrains/comments/187x4rx/is_there_a_hot_key_for_jumping_n_lines_while/

---

## How can I jump multiple lines at once in JetBrains IDEs easily?

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Grok-feb-2025-logo.svg/330px-Grok-feb-2025-logo.svg.png" class="logo" width="120" alt="Grok AI"/>

To jump multiple lines at once in JetBrains IDEs, you can use the **Jumping Lines** plugin, which allows customizable
line jumps with minimal keystrokes. Here’s how to set it up and use it:

1. **Install the Plugin**:
    - Go to **Settings > Plugins** in your IDE.
    - Search for "Jumping Lines" in the Marketplace and install it.[](https://github.com/HarshPanchal18/Jumping-Lines)

2. **Configure Shortcuts**:
    - After installation, access settings with **Alt + L**.
    - Set the number of lines to jump (e.g., 5, 10).
    - Default shortcuts:
        - **Ctrl + Shift + Period**: Jump forward.
        - **Ctrl + Shift + Comma**: Jump backward.
        - **Alt + Shift + Period**: Jump forward with selection.
        - **Alt + Shift + Comma**: Jump backward with selection.
        - **Ctrl + Comma**: Jump to the middle line of the editor.[](https://github.com/HarshPanchal18/Jumping-Lines)

3. **Alternative Without Plugin**:
    - Record a macro to move the cursor multiple lines:
        - Go to **Edit > Macros > Start Macro Recording**.
        - Press the **Up** or **Down** arrow key the desired number of times (e.g., 5).
        - Stop recording (**Edit > Macros > Stop Macro Recording**) and name the macro.
        - Assign a shortcut to the macro via **Settings > Keymap** (search for your macro
          name).[](https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000507430-How-to-jump-move-the-cursor-Up-or-Down-by-several-4-5-lines-in-Intellij)[](https://intellij-support.jetbrains.com/hc/en-us/community/posts/206347209-Move-caret-up-and-down-more-than-one-line)
    - Use **Page Up/Page Down** for larger jumps, though these may overshoot for small, precise
      jumps.[](https://intellij-support.jetbrains.com/hc/en-us/community/posts/206347209-Move-caret-up-and-down-more-than-one-line)

4. **Additional Tips**:
    - The plugin works across all JetBrains IDEs (IntelliJ IDEA, PyCharm, etc.) and supports jumping with or without
      text selection.[](https://github.com/HarshPanchal18/Jumping-Lines)
    - If you prefer keyboard-only navigation, consider the **IdeaVim** plugin for Vim-style jumps (e.g., `5j` to jump 5
      lines
      down).[](https://intellij-support.jetbrains.com/hc/en-us/community/posts/206347209-Move-caret-up-and-down-more-than-one-line)

* The **Jumping Lines** plugin is the easiest and most efficient solution, offering customizable jumps and compatibility
  with all JetBrains IDEs.[](https://github.com/HarshPanchal18/Jumping-Lines)

---

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Google_Gemini_logo.svg/330px-Google_Gemini_logo.svg.png" class="logo" width="120" alt="Google Gemini"/>

You can easily jump multiple lines at once in JetBrains IDEs using the following methods:

### 1. Using the "Jumping Lines" Plugin

This plugin allows you to navigate your code more efficiently by jumping multiple lines at once.

* **Installation:**
    1. Open your JetBrains IDE.
    2. Go to **Settings/Preferences** (`Ctrl+Alt+S` on Windows/Linux, `Cmd+,` on macOS).
    3. Select **Plugins**.
    4. In the Marketplace tab, search for "Jumping Lines" and click **Install**.
    5. Restart your IDE.
* **Usage:**
    * Jump 2 lines upward: `Ctrl+Shift+,`
    * Jump 4 lines downward: `Ctrl+Shift+.`
    * Jump to the middle line of the editor: `Ctrl+,`
    * Jump with selection (upward): `Ctrl+Shift+,`
    * Jump with selection (downward): `Ctrl+Shift+.`
    * You can configure the number of lines to jump in the plugin settings (`Alt+L` to open).

### 2. Using the "Jumper" Plugin

Similar to "Jumping Lines," this plugin lets you define custom jump actions for a specific number of lines.

* **Installation:**
    1. Open your JetBrains IDE.
    2. Go to **Settings/Preferences** (`Ctrl+Alt+S` on Windows/Linux, `Cmd+,` on macOS).
    3. Select **Plugins**.
    4. In the Marketplace tab, search for "Jumper" and click **Install**.
    5. Restart your IDE.

* **Usage:**
    1. Go to **Settings/Preferences** (`Ctrl+Alt+S` on Windows/Linux, `Cmd+,` on macOS) and navigate to **Tools > Jumper
       Settings**.
    2. Create a new jump action by specifying the number of lines to jump.
    3. Click the **"Change Keybindings"** button and assign a keyboard shortcut to your new jump action.

### 3. Using Multiple Carets (Basic Jumping)

While not a direct "jump," you can quickly place carets on multiple lines and then move them simultaneously.

* **Adding Carets:**
    * **Consecutive Lines:** Press `Ctrl` twice (and hold the second press) then press the **Up** or **Down** arrow
      keys. This will add carets above or below your current caret.
    * **Arbitrary Lines (with Mouse):** `Alt + Shift + Click` at the desired locations on different lines to add carets.
* **Moving Carets:** Once you have multiple carets, pressing the **Up** or **Down** arrow keys will move all carets
  simultaneously.

### 4. Column Selection Mode (for Block Jumps)

This mode allows you to select a rectangular block of text across multiple lines. While primarily for selection, it can
be used to quickly move the cursor to the beginning or end of a block.

* **Toggling Column Selection Mode:** `Alt + Shift + Insert` (Windows/Linux), `Cmd + Shift + 8` (macOS).
* **Selecting Multiple Lines in Column Mode:** After enabling column selection, hold `Shift` and use the **Up** or *
  *Down** arrow keys to select a block of lines.
* **Moving Caret to Start/End of Block:** Once the block is selected, pressing `Home` will move all carets to the
  beginning of their respective lines, and `End` will move them to the end. Remember to toggle column selection mode
  off (`Alt + Shift + Insert` or `Cmd + Shift + 8`) to return to normal editing.

By utilizing these methods, especially the dedicated plugins, you can significantly speed up your navigation within
JetBrains IDEs when dealing with multiple lines of code. Remember to explore the keymap settings (`Ctrl+Alt+S`
or `Cmd+,` then **Keymap**) to see if there are other navigation shortcuts that suit your workflow.

---

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/ChatGPT-Logo.svg/250px-ChatGPT-Logo.svg.png" class="logo" width="120" alt="ChatGPT"/>

Yes, there are plugins available for JetBrains IDEs that can simplify the functionality of jumping multiple lines at
once. One such plugin is **Jumping Lines**, which allows you to navigate through your code by jumping multiple lines
forward and backward with customizable settings.([GitHub][1])

### 🔌 Jumping Lines Plugin

**Key Features:**

* **Line Jumping:** Jump forward or backward multiple lines at a time.
* **Line Jumping with Selection:** Jump forward or backward multiple lines at a time with selection.
* **Customizable Number of Lines:** Define the number of lines to jump forward and backward through the plugin settings.
* **Jump to Middle Line:** Jump to the middle line of the editor.
* **Jump Score:** A score that counts the number of lines you've jumped.
* **Status Bar Widget:** View Jump Score directly from the status bar.
* **Compatibility:** Compatible with all JetBrains IDEs, including IntelliJ IDEA, PyCharm, Android Studio, PHPStorm, and
  RubyMine.([GitHub][1])

**Usage:**

1. **Install the Plugin:**

    * Go to `Settings -> Plugins` in your IDE.
    * Search for "Jumping Lines" and install it.
2. **Configure the Plugin:**

    * Navigate to `Settings -> Jumping Lines` or `Other Settings`.
    * Alternatively, go through `Tools -> Jumping Lines`, or press `Alt + L`.
3. **Start Navigating Using Keymaps:**

    * `Ctrl + Shift + Period` to jump forward.
    * `Ctrl + Shift + Comma` to jump backward.
    * `Alt + Shift + Period` to jump forward with selection.
    * `Alt + Shift + Comma` to jump backward with selection.
    * `Ctrl + Comma` to jump to the middle line of the editor.
    * `Alt + L` to open plugin settings.([GitHub][1])

This plugin provides a powerful and customizable way to navigate through your code efficiently.([GitHub][1])

**Reference:**

* [Jumping Lines Plugin on GitHub](https://github.com/HarshPanchal18/Jumping-Lines)([GitHub][1])

If you need assistance with installing or configuring the plugin, feel free to ask!

[1]: https://github.com/HarshPanchal18/Jumping-Lines?utm_source=chatgpt.com "HarshPanchal18/Jumping-Lines: A Jetbrains IDE plugin for ... - GitHub"