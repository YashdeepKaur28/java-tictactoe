import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TicTacToe extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(13, 13, 23);
    private static final Color PANEL_BG  = new Color(20, 20, 36);
    private static final Color CELL_BG   = new Color(28, 28, 48);
    private static final Color CELL_HVR  = new Color(38, 38, 68);
    private static final Color X_COLOR   = new Color(255, 80, 120);
    private static final Color O_COLOR   = new Color(60, 200, 255);
    private static final Color GRID_CLR  = new Color(50, 50, 80);
    private static final Color TEXT_CLR  = new Color(220, 220, 240);
    private static final Color DIM_CLR   = new Color(100, 100, 140);
    private static final Color WIN_GLOW  = new Color(255, 220, 60);

    // ── State ─────────────────────────────────────────────────────────────────
    private char[][] board = new char[3][3];
    private int currentPlayer = 1;          // 1 = X, 2 = O
    private boolean gameOver = false;
    private int[] winLine = null;           // indices of 3 winning cells (flat)
    private int p1Wins = 0, p2Wins = 0, draws = 0;

    // ── UI components ─────────────────────────────────────────────────────────
    private CellButton[] cells = new CellButton[9];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel turnIndicator;
    private JPanel boardPanel;

    public TicTacToe() {
        super("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initBoard();
        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = '-';
        gameOver = false;
        winLine = null;
        currentPlayer = 1;
    }

    // ── Build UI ──────────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBoard(),  BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Title
        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(loadFont(36f).deriveFont(Font.BOLD));
        title.setForeground(TEXT_CLR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score strip
        scoreLabel = new JLabel(scoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        scoreLabel.setForeground(DIM_CLR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Turn indicator
        turnIndicator = new JLabel(turnText(), SwingConstants.CENTER);
        turnIndicator.setFont(loadFont(15f));
        turnIndicator.setForeground(playerColor(currentPlayer));
        turnIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(turnIndicator);
        return panel;
    }

    private JPanel buildBoard() {
        boardPanel = new JPanel(new GridLayout(3, 3, 10, 10)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (winLine != null) drawWinLine(g);
            }
        };
        boardPanel.setBackground(BG);
        boardPanel.setPreferredSize(new Dimension(390, 390));

        for (int i = 0; i < 9; i++) {
            final int idx = i;
            CellButton btn = new CellButton();
            btn.addActionListener(e -> handleClick(idx));
            cells[i] = btn;
            boardPanel.add(btn);
        }
        return boardPanel;
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(loadFont(15f).deriveFont(Font.BOLD));
        statusLabel.setForeground(WIN_GLOW);

        JButton restart = new StyledButton("New Game");
        restart.addActionListener(e -> restartGame());

        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(restart, BorderLayout.EAST);
        return panel;
    }

    // ── Game logic ────────────────────────────────────────────────────────────
    private void handleClick(int idx) {
        if (gameOver) return;
        int r = idx / 3, c = idx % 3;
        if (board[r][c] != '-') return;

        board[r][c] = (currentPlayer == 1) ? 'X' : 'O';
        cells[idx].setMark(board[r][c], playerColor(currentPlayer));

        int[] win = checkWin();
        int filled = countFilled();

        if (win != null) {
            winLine = win;
            highlightWin(win);
            String winner = "Player " + currentPlayer + " (" + (currentPlayer == 1 ? "X" : "O") + ") wins!";
            statusLabel.setText("🏆 " + winner);
            if (currentPlayer == 1) p1Wins++; else p2Wins++;
            scoreLabel.setText(scoreText());
            turnIndicator.setText(" ");
            gameOver = true;
            boardPanel.repaint();
        } else if (filled == 9) {
            draws++;
            statusLabel.setText("It's a Draw!");
            scoreLabel.setText(scoreText());
            turnIndicator.setText(" ");
            gameOver = true;
        } else {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            turnIndicator.setText(turnText());
            turnIndicator.setForeground(playerColor(currentPlayer));
        }
    }

    private int[] checkWin() {
        int[][] lines = {
            {0,1,2},{3,4,5},{6,7,8},   // rows
            {0,3,6},{1,4,7},{2,5,8},   // cols
            {0,4,8},{2,4,6}            // diags
        };
        for (int[] l : lines) {
            char a = board[l[0]/3][l[0]%3];
            char b = board[l[1]/3][l[1]%3];
            char c = board[l[2]/3][l[2]%3];
            if (a != '-' && a == b && b == c) return l;
        }
        return null;
    }

    private int countFilled() {
        int n = 0;
        for (char[] row : board) for (char ch : row) if (ch != '-') n++;
        return n;
    }

    private void highlightWin(int[] line) {
        for (int i : line) cells[i].setWinner(true);
    }

    private void restartGame() {
        initBoard();
        for (CellButton c : cells) c.reset();
        statusLabel.setText(" ");
        turnIndicator.setText(turnText());
        turnIndicator.setForeground(playerColor(currentPlayer));
        boardPanel.repaint();
    }

    // ── Draw win line overlay ─────────────────────────────────────────────────
    private void drawWinLine(Graphics g) {
        if (winLine == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        CellButton c0 = cells[winLine[0]];
        CellButton c2 = cells[winLine[2]];

        int x1 = c0.getX() + c0.getWidth()  / 2;
        int y1 = c0.getY() + c0.getHeight() / 2;
        int x2 = c2.getX() + c2.getWidth()  / 2;
        int y2 = c2.getY() + c2.getHeight() / 2;

        g2.setColor(WIN_GLOW);
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        g2.dispose();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Color playerColor(int p) { return p == 1 ? X_COLOR : O_COLOR; }
    private String turnText() {
        return "Player " + currentPlayer + "'s turn  (" + (currentPlayer == 1 ? "X" : "O") + ")";
    }
    private String scoreText() {
        return "X  " + p1Wins + "   —   Draws " + draws + "   —   " + p2Wins + "  O";
    }

    private Font loadFont(float size) {
        // Fall back gracefully
        return new Font("Dialog", Font.PLAIN, (int) size);
    }

    // ── Inner: CellButton ─────────────────────────────────────────────────────
    class CellButton extends JButton {
        private char mark = '-';
        private Color markColor = TEXT_CLR;
        private boolean winner = false;
        private boolean hovered = false;

        CellButton() {
            setOpaque(true);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 120));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }

        void setMark(char m, Color c) { mark = m; markColor = c; repaint(); }
        void setWinner(boolean w)     { winner = w; repaint(); }
        void reset() { mark = '-'; winner = false; hovered = false; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int arc = 16;

            // Background
            Color bg = winner ? new Color(50, 44, 20)
                       : hovered && mark == '-' ? CELL_HVR
                       : CELL_BG;
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Border
            g2.setColor(winner ? WIN_GLOW : GRID_CLR);
            g2.setStroke(new BasicStroke(winner ? 2f : 1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            // Mark
            if (mark == 'X') drawX(g2, w, h);
            else if (mark == 'O') drawO(g2, w, h);

            g2.dispose();
        }

        private void drawX(Graphics2D g2, int w, int h) {
            int pad = 28;
            g2.setColor(markColor);
            g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // Glow pass
            g2.setColor(new Color(markColor.getRed(), markColor.getGreen(), markColor.getBlue(), 60));
            g2.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(pad, pad, w - pad, h - pad);
            g2.drawLine(w - pad, pad, pad, h - pad);
            // Crisp pass
            g2.setColor(markColor);
            g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(pad, pad, w - pad, h - pad);
            g2.drawLine(w - pad, pad, pad, h - pad);
        }

        private void drawO(Graphics2D g2, int w, int h) {
            int pad = 24;
            // Glow pass
            g2.setColor(new Color(markColor.getRed(), markColor.getGreen(), markColor.getBlue(), 60));
            g2.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(pad, pad, w - 2 * pad, h - 2 * pad);
            // Crisp pass
            g2.setColor(markColor);
            g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(pad, pad, w - 2 * pad, h - 2 * pad);
        }
    }

    // ── Inner: StyledButton ───────────────────────────────────────────────────
    class StyledButton extends JButton {
        private boolean hovered = false;

        StyledButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Monospaced", Font.BOLD, 13));
            setForeground(TEXT_CLR);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 36));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(hovered ? new Color(60, 60, 100) : new Color(40, 40, 70));
            g2.fillRoundRect(0, 0, w, h, 10, 10);
            g2.setColor(GRID_CLR);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new TicTacToe();
        });
    }
}