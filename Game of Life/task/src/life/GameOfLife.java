package life;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class GameOfLife extends JFrame {
    private static final int BORDER = 10;
    private final int headerPanelWidth = 200;
    private int boardSize = 50;
    private UniverseGenerator generator;
    private int currentGen = 0;
    private boolean paused;
    private double speed = 1;
    
    private final JLabel generationLabel;
    private final JLabel aliveLabel;
    
    public GameOfLife() {
        super("Game Of Life");
        int currentY = BORDER;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300 + headerPanelWidth, 300);
        setMinimumSize(new Dimension(300 + headerPanelWidth, 300));
        setLayout(null);
        
        // Buttons Start
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        buttonsPanel.setBounds(BORDER, currentY, headerPanelWidth - BORDER, 40);
        
        Button reloadButton = new Button();
        reloadButton.setLabel("Reset");
        reloadButton.setName("ResetButton");
        reloadButton.addActionListener(actionEvent -> startSim());
        buttonsPanel.add(reloadButton);
        
        Button pauseButton = new Button();
        pauseButton.setLabel("Pause");
        pauseButton.setName("PlayToggleButton");
        pauseButton.addActionListener(actionEvent -> {
            paused = !paused;
            pauseButton.setLabel(paused ? "Play" : "Paused");
        });
        buttonsPanel.add(pauseButton);
        
        Button nextButton = new Button();
        nextButton.setLabel("Next");
        nextButton.setName("NextButton");
        nextButton.addActionListener(actionEvent -> {
            generator.advanceGeneration();
            currentGen++;
            updateGraphics(currentGen);
        });
        buttonsPanel.add(nextButton);
        add(buttonsPanel);
        // Buttons End
        
        // Header Start
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBounds(BORDER, currentY += 40, headerPanelWidth - BORDER, 40);
        
        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(generationLabel);
        
        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        headerPanel.add(aliveLabel);
        
        add(headerPanel);
        // Header End
        
        // Speed Control Start
        JPanel speedControlPanel = new JPanel();
        speedControlPanel.setBounds(BORDER, currentY += 40, headerPanelWidth - BORDER, 40);
        speedControlPanel.setLayout(new BoxLayout(speedControlPanel, BoxLayout.Y_AXIS));
        
        JLabel speedLabel = new JLabel("Speed: ");
        speedControlPanel.add(speedLabel);
        
        JSlider speedSlider = new JSlider();
        speedSlider.setMinimum(5);
        speedSlider.setMaximum(15);
        speedSlider.setValue(10);
        speedSlider.addChangeListener(changeEvent -> speed = speedSlider.getValue() / 10.0);
        speedControlPanel.add(speedSlider);
        
        add(speedControlPanel);
        // Speed Control End
        
        // Size Control Start
        JPanel sizeControlPanel = new JPanel();
        sizeControlPanel.setBounds(BORDER, currentY + 40, headerPanelWidth - BORDER, 40);
        sizeControlPanel.setLayout(new BoxLayout(sizeControlPanel, BoxLayout.Y_AXIS));
        
        JLabel sizeLabel = new JLabel("Size (requires restart): ");
        sizeControlPanel.add(sizeLabel);
        
        JTextArea sizeTextArea = new JTextArea();
        sizeTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
            
            }
            
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
            
            }
            
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                try {
                    boardSize = Integer.parseInt(sizeTextArea.getText());
                } catch (NumberFormatException ignored) {
                }
            }
        });
        sizeTextArea.setText("20");
        sizeTextArea.setBorder(new LineBorder(Color.BLACK, 1));
        sizeControlPanel.add(sizeTextArea);
        
        add(sizeControlPanel);
        // Size Control End
        
        setVisible(true);
        
        startSim();
        while (true) {
            if (!paused) {
                generator.advanceGeneration();
                currentGen++;
                updateGraphics(currentGen);
            }
            
            try {
                Thread.sleep((long) (1000 / speed));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void startSim() {
        currentGen = 0;
        generator = new UniverseGenerator(boardSize);
    }
    
    private void updateGraphics(int currentGen) {
        generationLabel.setText("Generation #" + (currentGen + 1));
        aliveLabel.setText("Alive: " + generator.countAlive());
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int totalX = getWidth() - headerPanelWidth - BORDER * 2;
        int totalY = getHeight() - BORDER * 2;
        int startX = headerPanelWidth + BORDER;
        int startY = BORDER + 25;
        int cellSize = Math.min(totalX, totalY) / boardSize;
        
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (generator.getUniverseCell(x, y)) {
                    g.fillRect(startX + cellSize * x, startY + cellSize * y, cellSize, cellSize);
                } else {
                    g.drawRect(startX + cellSize * x, startY + cellSize * y, cellSize, cellSize);
                }
            }
        }
    }
}
