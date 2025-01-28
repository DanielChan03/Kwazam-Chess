package view;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.*;
import java.awt.*;

public class BoardGUI extends JPanel {

    private final int MAX_ROW = 8;
    private final int MAX_COLS = 5;
    private HighlightableButton[][] buttons;
    private String[][] pieceName;
    private boolean isFlipped = false;
    private boolean[][] isReversed; // 2D array to track Ram reverse state

    BoardGUI() {

        setLayout(new GridLayout(MAX_ROW, MAX_COLS));
        pieceName = new String[MAX_ROW][MAX_COLS];
        isReversed = new boolean[MAX_ROW][MAX_COLS];
        setBackground(new Color(245, 245, 220));

        int buttonSize = 60;

        buttons = new HighlightableButton[MAX_ROW][MAX_COLS];
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                buttons[i][j] = new HighlightableButton();
                buttons[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(new Color(240, 217, 181)); // Light (Beige)
                } else {
                    buttons[i][j].setBackground(new Color(181, 136, 99)); // Dark (Brown)
                }

                buttons[i][j].setOpaque(true);
                add(buttons[i][j]);
            }
        }
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public void initialiseBoard() {

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                resetPieceImage(row, col);
            }
        }

        setPieceImage(0, 0, "r_tor");
        setPieceImage(0, 1, "r_biz");
        setPieceImage(0, 3, "r_biz");
        setPieceImage(0, 2, "r_sau");
        setPieceImage(0, 4, "r_xor");
        for (int i = 0; i < 5; i++)
            setPieceImage(1, i, "r_ram");

        setPieceImage(7, 0, "b_xor");
        setPieceImage(7, 1, "b_biz");
        setPieceImage(7, 3, "b_biz");
        setPieceImage(7, 2, "b_sau");
        setPieceImage(7, 4, "b_tor");
        for (int i = 0; i < 5; i++)
            setPieceImage(6, i, "b_ram");
    }

    public void setFlip(boolean flip) {
        this.isFlipped = flip;
    }

    public void setReverse(int row, int col, boolean reverse) {
        this.isReversed[row][col] = reverse;
    }

    public boolean getIsFlipped() {
        return isFlipped;
    }

    public boolean getIsReversed(int row, int col) {
        return isReversed[row][col];
    }

    public void setPieceImage(int row, int col, String pieceName) {
        if (pieceName == null) {
            this.buttons[row][col].setIcon(null);
            this.pieceName[row][col] = null;
            this.isReversed[row][col] = false;
        } else {

            URL imageUrl = getClass().getResource("/resources/images/" + pieceName + ".png");
            if (imageUrl == null) {
                System.out.println("Error loading image: /resources/images/" + pieceName + ".png");
                return;
            }

            ImageIcon originalIcon = new ImageIcon(imageUrl);


            Image originalImage = originalIcon.getImage();

            // Apply flipping transformation if needed
            if (isFlipped) {
                BufferedImage bufferedImage = toBufferedImage(originalImage);
                AffineTransform transform = AffineTransform.getScaleInstance(-1, -1);
                transform.translate(-bufferedImage.getWidth(), -bufferedImage.getHeight());
                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                bufferedImage = op.filter(bufferedImage, null);
                originalImage = bufferedImage;
            }

            // Apply additional transformation for reversed RamPiece
        if (pieceName.contains("ram") && isReversed[row][col]) {
            originalImage = applyRamReversal(originalImage);
        }

            Image resizedImage = originalImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            this.buttons[row][col].setIcon(resizedIcon);
            this.pieceName[row][col] = pieceName;
        }
    }

    private BufferedImage toBufferedImage(Image image) {

        if (image == null) {
            return null;
        }
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    private Image applyRamReversal(Image image) {
        BufferedImage bufferedImage = toBufferedImage(image);
        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(180),
                bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    public void resetPieceImage(int row, int col) {
        this.buttons[row][col].setIcon(null); // Removes the image at the old position
        this.pieceName[row][col] = null; // Remove the piece name from the internal state
    }

    public String getPieceName(int row, int col) {
        return pieceName[row][col];
    }


    // Resizing board
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the size of the chessboard to maintain aspect ratio
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Aspect ratio of the chessboard (5 columns x 8 rows)
        double aspectRatio = (double) MAX_COLS / MAX_ROW;

        // Calculate the size of the chessboard
        int chessboardWidth, chessboardHeight;
        if (panelWidth / aspectRatio < panelHeight) {
            // Width is the limiting factor
            chessboardWidth = panelWidth;
            chessboardHeight = (int) (panelWidth / aspectRatio);
        } else {
            // Height is the limiting factor
            chessboardHeight = panelHeight;
            chessboardWidth = (int) (panelHeight * aspectRatio);
        }

        // Center the chessboard within the panel
        int x = (panelWidth - chessboardWidth) / 2;
        int y = (panelHeight - chessboardHeight) / 2;

        // Resize and reposition the buttons
        int buttonWidth = chessboardWidth / MAX_COLS;
        int buttonHeight = chessboardHeight / MAX_ROW;

        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                // Calculate the row and column based on flip state
                int row = isFlipped ? MAX_ROW - 1 - i : i;
                int col = isFlipped ? MAX_COLS - 1 - j : j;

                buttons[row][col].setBounds(
                        x + j * buttonWidth, // x position
                        y + i * buttonHeight, // y position
                        buttonWidth, // width
                        buttonHeight // height
                );

            }
        }
    }
}
