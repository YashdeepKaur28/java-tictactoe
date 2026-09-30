# Tic Tac Toe — Modern Java Swing Game 🎮

A polished desktop Tic-Tac-Toe game built with **Java Swing** and **Graphics2D**, featuring a dark modern theme, custom-painted X/O marks with glow effects, animated win-line overlay, and persistent score tracking across rounds.

## 📸 Preview

<img width="456" height="660" alt="image" src="https://github.com/user-attachments/assets/4d30b0ce-2a21-47f3-8834-ab9c99f62acf" />


## 📖 Overview

**Tic Tac Toe** is a two-player desktop game that demonstrates clean Java Swing architecture, custom component painting, and modern UI design — all without any external libraries or image assets. Every visual element (X, O, hover states, win glow) is rendered programmatically with `Graphics2D`.

Built to practice:
- **Swing architecture** — `JFrame`, `JPanel`, `JButton`, layout managers
- **Custom painting** — `paintComponent`, `Graphics2D`, `RenderingHints`
- **Event handling** — `ActionListener`, `MouseListener`, `MouseAdapter`
- **Encapsulation** — inner classes (`CellButton`, `StyledButton`) as reusable UI components
- **Game state management** — board array, win detection, turn logic


## ✨ Features

- 🎨 **Dark modern theme** with a hand-picked color palette
- ✏️ **Custom-painted X and O** with double-pass glow effect (no image assets)
- 🖱️ **Hover feedback** on empty cells
- 🏆 **Animated win-line overlay** drawn across the winning cells
- 🟡 **Winner highlight** — winning cells glow in gold
- 📊 **Score tracking** — persistent X wins / O wins / draws across rounds
- 🎯 **Turn indicator** — shows whose turn it is, in that player's color
- 🔁 **New Game button** with custom styling
- ⚡ **Anti-aliased rendering** for smooth edges
- 🖥️ **System L&F fallback** — adapts to OS theme where possible
- 📌 **Non-resizable window**, centered on launch


## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java (JDK 8+) |
| GUI | Swing (`JFrame`, `JPanel`, `JButton`, `JLabel`) |
| Rendering | `Graphics2D`, `RenderingHints`, `BasicStroke` |
| Layout | `BorderLayout`, `BoxLayout`, `GridLayout` |
| Events | `ActionListener`, `MouseAdapter` |
| Design | OOP with inner component classes |


## 🎮 How to Play

1. Launch the app — the window opens centered on your screen.
2. **Player X** goes first.
3. Click any empty cell to place your mark.
4. The turn indicator updates in the current player's color.
5. When a player gets **3 in a row** (row, column, or diagonal):
   - The winning cells glow **gold**
   - A **win line** is drawn across them
   - The score counter updates
6. If all 9 cells are filled with no winner, it's a **draw**.
7. Click **New Game** to reset the board (scores persist).


## 🧠 Key Concepts Demonstrated

- **Custom component painting:** Both `CellButton` and `StyledButton` override `paintComponent` to draw rounded rectangles, gradients, and glows — no images required
- **Double-pass rendering:** Each mark is drawn twice — a thick semi-transparent stroke for glow, then a crisp thin stroke on top
- **Game state as a class:** Board, current player, winner line, and scores are all encapsulated in the `TicTacToe` frame
- **Win detection:** 8 winning lines checked via a flat index array
- **Overlay drawing:** The win line is painted on top of the grid using `boardPanel`'s overridden `paintComponent`
- **Event delegation:** A single `ActionListener` handles all 9 cells via `final int idx` capture in a loop
- **UI polish details:** `setContentAreaFilled(false)`, custom cursors, anti-aliasing, and hover states


## 🚀 Future Enhancements

- [ ] Add an **AI opponent** (minimax algorithm) for single-player mode
- [ ] Add **sound effects** for moves and wins
- [ ] Add a **difficulty selector** (Easy / Medium / Impossible)
- [ ] Add **move history / undo**
- [ ] Animate mark placement (scale-in effect)
- [ ] Replace `Board` array with a proper `Board` class
- [ ] Add **unit tests** with JUnit for `checkWin()` and win-detection logic
- [ ] Export as a runnable JAR with `jar cfe`


## 👩‍💻 Author

**Yashdeep Kaur**
- 🎓 B.Tech CSE, Punjabi University, Patiala (2026)
- 💼 Java Full Stack Trainee @ CodeSquadz
- 📧 Email: ykdeep2453@gmail.com
- 🔗 LinkedIn: [yashdeep-kaur-16aa083b1](https://linkedin.com/in/yashdeep-kaur-16aa083b1)
- 🐙 GitHub: [@YashdeepKaur28](https://github.com/YashdeepKaur28)


⭐ If you liked this game, consider giving it a star!
