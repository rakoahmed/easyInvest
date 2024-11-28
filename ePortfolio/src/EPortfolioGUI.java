package rako_a3.ePortfolio.src;

import java.awt.*; // For layouts, colors, fonts, etc.
import java.awt.event.*; // For event handling (e.g., WindowAdapter, ActionListener)
import java.util.List; // For handling lists (e.g., List<Investment>)
import javax.swing.*; // For GUI components like JFrame, JPanel, JButton, etc.
import javax.swing.border.TitledBorder; // For titled borders in the GUI






/**
 * The EPortfolioGUI class handles the GUI for the ePortfolio application.
 * It provides interfaces for buying, selling, updating investments,
 * calculating total gains, and searching for investments.
 * 
 * The GUI is implemented in a single Java file.
 * Exception handling is also included.
 * 
 * Commands to compile and run:
 * Compile: javac -d ePortfolio/bin ePortfolio/src/*.java
 * Run: java -cp ePortfolio/bin rako_a3.ePortfolio.src.Main investments.txt 
 * 
 * Note: This class requires Java Swing (javax.swing) and AWT (java.awt) packages.
 * 
 * @author  Rako Ahmed
 * @version 3.0
 */
public class EPortfolioGUI extends JFrame {
    private Portfolio portfolio;
    private String filename;
    private CardLayout cardLayout; // To manage switching between panels
    private JPanel mainPanel;     // Main panel using CardLayout


    public EPortfolioGUI(Portfolio portfolio, String filename) {
        this.portfolio = portfolio;
        this.filename = filename;
        initializeGUI();
    }

    /**
     * Initializes the main graphical user interface (GUI) components for the application.
     * Sets up the menu, main panel, and default layout for the application.
     * Also applies the Nimbus look-and-feel for a modern UI experience.
     */        
    private void initializeGUI() {
        setTitle("ePortfolio");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add a WindowListener to handle the red X button
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    EPortfolioGUI.this,
                    "Do you want to save and exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    portfolio.saveInvestments(filename); // Save the portfolio
                    System.exit(0); // Exit the application
                }
            }
        });
        

        // Create menu
        JMenuBar menuBar = new JMenuBar();
        JMenu commandsMenu = new JMenu("Commands");
        JMenuItem buyItem = new JMenuItem("Buy");
        JMenuItem sellItem = new JMenuItem("Sell");
        JMenuItem updateItem = new JMenuItem("Update");
        JMenuItem getGainItem = new JMenuItem("Get Gain");
        JMenuItem searchItem = new JMenuItem("Search");
        JMenuItem quitItem = new JMenuItem("Quit");

        // Add menu items to menu
        commandsMenu.add(buyItem);
        commandsMenu.add(sellItem);
        commandsMenu.add(updateItem);
        commandsMenu.add(getGainItem);
        commandsMenu.add(searchItem);
        commandsMenu.add(quitItem);

        // Add menu to menu bar
        menuBar.add(commandsMenu);
        setJMenuBar(menuBar);

        // Add action listeners
        buyItem.addActionListener(e -> showBuyInterface());
        sellItem.addActionListener(e -> showSellInterface());
        updateItem.addActionListener(e -> showUpdateInterface());
        getGainItem.addActionListener(e -> showGetGainInterface());
        searchItem.addActionListener(e -> showSearchInterface());
        quitItem.addActionListener(e -> {
            portfolio.saveInvestments(filename);
            System.exit(0);
        });

        // Create mainPanel with CardLayout
        cardLayout = new CardLayout(); // Initialize CardLayout
        mainPanel = new JPanel(cardLayout); // Create JPanel with CardLayout
        add(mainPanel, BorderLayout.CENTER); // Add mainPanel to JFrame
          

        // Show welcome screen
        showWelcomeScreen();

        setVisible(true);
    }


    /**
     * Displays the welcome screen for the application.
     * Provides a brief introduction and instructions for using the application.
     * Centers the message for better visual appeal.
     */
    private void showWelcomeScreen() {
        getContentPane().removeAll();
    
        // Create a panel with GridBagLayout to center components
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(Color.WHITE);
    
        // Welcome message with larger font
        JTextArea welcomeText = new JTextArea();
        welcomeText.setEditable(false);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font size
        welcomeText.setText("Welcome to ePortfolio!\n\n" +
                "Choose a command from the “Commands” menu to buy or sell\n" +
                "an investment, update prices for all investments, get gain for the\n" +
                "portfolio, search for relevant investments, or quit the program.");
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);
    
        // Add padding around the text
        welcomeText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(welcomeText);
        scrollPane.setBorder(null); // Remove border for a cleaner look
    
        // Add the scroll pane to the center of the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH; // Allow it to resize
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        welcomePanel.add(scrollPane, gbc);
    
        // Add the panel to the frame
        add(welcomePanel, BorderLayout.CENTER);
    
        // Refresh the frame
        revalidate();
        repaint();
    }
    
    /**
     * Displays the interface for buying an investment.
     * Includes input fields for investment details (type, symbol, name, quantity, price),
     * a message area for feedback, and buttons to reset or submit the input.
     * Validates user input and processes the purchase transaction.
     */
    private void showBuyInterface() {
        getContentPane().removeAll();

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Buy Investment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Input fields panel with GridBagLayout for better alignment
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Investment Information", TitledBorder.LEFT, TitledBorder.TOP));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input fields
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Stock", "MutualFund"});
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(20);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(20);
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(20);

        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(typeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(symbolLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(symbolField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        JButton resetButton = new JButton("Reset");
        JButton buyButton = new JButton("Buy");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        buyButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(resetButton);
        buttonPanel.add(buyButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Message area
        JTextArea messageArea = new JTextArea(8, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Messages", TitledBorder.LEFT, TitledBorder.TOP));
        mainPanel.add(scrollPane, BorderLayout.EAST);

        // Action listeners
        resetButton.addActionListener(e -> {
            symbolField.setText("");
            nameField.setText("");
            quantityField.setText("");
            priceField.setText("");
            typeComboBox.setSelectedIndex(0);
            messageArea.setText("");
        });

        buyButton.addActionListener(e -> {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                String symbol = symbolField.getText().trim();
                String name = nameField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                if (symbol.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException("Symbol and name cannot be empty.");
                }

                Investment existingInv = portfolio.findInvestmentBySymbol(symbol);
                if (existingInv == null) {
                    Investment newInv;
                    if (type.equalsIgnoreCase("Stock")) {
                        newInv = new Stock(symbol, name, quantity, price);
                    } else {
                        newInv = new MutualFund(symbol, name, quantity, price);
                    }
                    portfolio.addInvestment(newInv);
                    messageArea.setText("Purchase successful.");
                } else {
                    if ((type.equalsIgnoreCase("Stock") && existingInv instanceof Stock)
                            || (type.equalsIgnoreCase("MutualFund") && existingInv instanceof MutualFund)) {
                        existingInv.buy(quantity, price);
                        messageArea.setText("Added to existing investment.");
                    } else {
                        throw new IllegalArgumentException("Investment type mismatch with existing investment.");
                    }
                }
            } catch (NumberFormatException ex) {
                messageArea.setText("Invalid number format: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                messageArea.setText("Error: " + ex.getMessage());
            }
        });

        // Add main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }

    /**
     * Displays the interface for selling an investment.
     * Includes input fields for investment details (symbol, quantity, price),
     * a message area for feedback, and buttons to reset or submit the input.
     * Validates user input and processes the sale transaction.
     */

    private void showSellInterface() {
        getContentPane().removeAll();
    
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
    
        // Title Label
        JLabel titleLabel = new JLabel("Sell Investment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Input fields panel with GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Investment Information", TitledBorder.LEFT, TitledBorder.TOP));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Input fields with explicit preferred size
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField();
        symbolField.setPreferredSize(new Dimension(250, 30));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        quantityField.setPreferredSize(new Dimension(250, 30));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(250, 30));
    
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(symbolLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(symbolField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);
    
        mainPanel.add(inputPanel, BorderLayout.WEST);
    
        // Message area
        JTextArea messageArea = new JTextArea(10, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Messages", TitledBorder.LEFT, TitledBorder.TOP));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        JButton resetButton = new JButton("Reset");
        JButton sellButton = new JButton("Sell");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        sellButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(resetButton);
        buttonPanel.add(sellButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Action listeners
        resetButton.addActionListener(e -> {
            symbolField.setText("");
            quantityField.setText("");
            priceField.setText("");
            messageArea.setText("");
        });
    
        sellButton.addActionListener(e -> {
            try {
                String symbol = symbolField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
    
                if (symbol.isEmpty()) {
                    throw new IllegalArgumentException("Symbol cannot be empty.");
                }
    
                Investment inv = portfolio.findInvestmentBySymbol(symbol);
                if (inv == null) {
                    throw new IllegalArgumentException("Investment not found.");
                }
    
                double payment = inv.sell(quantity, price);
                messageArea.setText("Sale successful. Payment received: $" + String.format("%.2f", payment));
                if (inv.getQuantity() == 0) {
                    portfolio.removeInvestment(inv);
                    messageArea.append("\nInvestment sold out and removed from portfolio.");
                }
            } catch (NumberFormatException ex) {
                messageArea.setText("Invalid number format: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                messageArea.setText("Error: " + ex.getMessage());
            }
        });
    
        // Add main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }
    
    
    /**
     * Displays the interface for updating investment prices.
     * Shows investment details (symbol, name, price) and allows the user to
     * navigate between investments and update their prices.
     * Includes validation for price updates and feedback messages.
     */

    private int currentIndex = 0;

    private void showUpdateInterface() {
        getContentPane().removeAll();
    
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
    
        // Title Label
        JLabel titleLabel = new JLabel("Update Investment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Input fields panel with GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Investment Information", TitledBorder.LEFT, TitledBorder.TOP));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Input fields
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField();
        symbolField.setEditable(false);
        symbolField.setPreferredSize(new Dimension(250, 30));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        nameField.setEditable(false);
        nameField.setPreferredSize(new Dimension(250, 30));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(250, 30));
    
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(symbolLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(symbolField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);
    
        mainPanel.add(inputPanel, BorderLayout.WEST);
    
        // Message area
        JTextArea messageArea = new JTextArea(10, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Messages", TitledBorder.LEFT, TitledBorder.TOP));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        JButton saveButton = new JButton("Save");
        prevButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Handle empty investments
        List<Investment> investments = portfolio.getInvestments();
        if (investments.isEmpty()) {
            messageArea.setText("No investments to update.");
            add(mainPanel);
            revalidate();
            repaint();
            return;
        }
    
        // Display first investment
        displayInvestment(investments.get(currentIndex), symbolField, nameField, priceField);
    
        // Update buttons state
        updatePrevNextButtons(prevButton, nextButton, investments.size());
    
        // Action listeners
        prevButton.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayInvestment(investments.get(currentIndex), symbolField, nameField, priceField);
                updatePrevNextButtons(prevButton, nextButton, investments.size());
                messageArea.setText("");
            }
        });
    
        nextButton.addActionListener(e -> {
            if (currentIndex < investments.size() - 1) {
                currentIndex++;
                displayInvestment(investments.get(currentIndex), symbolField, nameField, priceField);
                updatePrevNextButtons(prevButton, nextButton, investments.size());
                messageArea.setText("");
            }
        });
    
        saveButton.addActionListener(e -> {
            try {
                double newPrice = Double.parseDouble(priceField.getText().trim());
                if (newPrice < 0) {
                    throw new IllegalArgumentException("Price cannot be negative.");
                }
                Investment inv = investments.get(currentIndex);
                inv.setPrice(newPrice);
                messageArea.setText("Price updated successfully.\n" + inv.toString());
            } catch (NumberFormatException ex) {
                messageArea.setText("Invalid number format: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                messageArea.setText("Error: " + ex.getMessage());
            }
        });
    
        // Add main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }
    
    /**
     * Populates the input fields with the details of the given investment.
     * 
     * @param inv The investment whose details need to be displayed.
     * @param symbolField The text field for the investment's symbol.
     * @param nameField The text field for the investment's name.
     * @param priceField The text field for the investment's price.
     */
    private void displayInvestment(Investment inv, JTextField symbolField, JTextField nameField, JTextField priceField) {
        symbolField.setText(inv.getSymbol());
        nameField.setText(inv.getName());
        priceField.setText(String.valueOf(inv.getPrice()));
    }


    /**
     * Updates the state (enabled/disabled) of the "Prev" and "Next" buttons
     * based on the current position in the investment list.
     * 
     * @param prevButton The "Prev" button to be updated.
     * @param nextButton The "Next" button to be updated.
     * @param size The total number of investments in the list.
     */
    private void updatePrevNextButtons(JButton prevButton, JButton nextButton, int size) {
        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < size - 1);
    }


    /**
     * Displays the interface for calculating and viewing total gains.
     * Shows the total gain across all investments and provides details
     * of individual gains for each investment.
     * Includes a message area for displaying the calculated results.
     */
    private void showGetGainInterface() {
        getContentPane().removeAll();
    
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
    
        // Title Label
        JLabel titleLabel = new JLabel("Total Gains");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Total gain field panel with GridBagLayout for alignment
        JPanel gainPanel = new JPanel(new GridBagLayout());
        gainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Total Gain", TitledBorder.LEFT, TitledBorder.TOP));
        gainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel totalGainLabel = new JLabel("Total Gain:");
        JTextField totalGainField = new JTextField(20);
        totalGainField.setEditable(false);
        totalGainField.setPreferredSize(new Dimension(250, 30));
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gainPanel.add(totalGainLabel, gbc);
        gbc.gridx = 1;
        gainPanel.add(totalGainField, gbc);
    
        mainPanel.add(gainPanel, BorderLayout.WEST);
    
        // Messages area
        JTextArea messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Investment Gains Details", TitledBorder.LEFT, TitledBorder.TOP));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Calculate total gain and individual gains
        double totalGain = 0;
        StringBuilder gainsDetails = new StringBuilder();
        List<Investment> investments = portfolio.getInvestments();
        for (Investment inv : investments) {
            double gain = inv.getGain();
            totalGain += gain;
            gainsDetails.append(inv.getSymbol()).append(" Gain: $").append(String.format("%.2f", gain)).append("\n");
        }
        totalGainField.setText("$" + String.format("%.2f", totalGain));
        messageArea.setText(gainsDetails.toString());
    
        // Add main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }
    


    /**
     * Displays the interface for searching investments.
     * Includes input fields for search criteria (symbol, name keywords, price range),
     * a message area for displaying search results, and buttons to reset or submit the search query.
     * Validates the user input and executes the search operation in the portfolio.
     */
    private void showSearchInterface() {
        getContentPane().removeAll();
    
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
    
        // Title Label
        JLabel titleLabel = new JLabel("Search Investments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Input fields panel with GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Search Criteria", TitledBorder.LEFT, TitledBorder.TOP));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Input fields
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField();
        symbolField.setPreferredSize(new Dimension(250, 30));
        JLabel keywordsLabel = new JLabel("Name Keywords:");
        JTextField keywordsField = new JTextField();
        keywordsField.setPreferredSize(new Dimension(250, 30));
        JLabel priceRangeLabel = new JLabel("Price Range (e.g., 100-500):");
        JTextField priceRangeField = new JTextField();
        priceRangeField.setPreferredSize(new Dimension(250, 30));
    
        // Add components to input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(symbolLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(symbolField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(keywordsLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(keywordsField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(priceRangeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(priceRangeField, gbc);
    
        mainPanel.add(inputPanel, BorderLayout.WEST);
    
        // Message area for search results
        JTextArea resultsArea = new JTextArea(15, 40);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Search Results", TitledBorder.LEFT, TitledBorder.TOP));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        JButton resetButton = new JButton("Reset");
        JButton searchButton = new JButton("Search");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(resetButton);
        buttonPanel.add(searchButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Action listeners
        resetButton.addActionListener(e -> {
            symbolField.setText("");
            keywordsField.setText("");
            priceRangeField.setText("");
            resultsArea.setText("");
        });
    
        searchButton.addActionListener(e -> {
            try {
                String symbol = symbolField.getText().trim();
                String keywords = keywordsField.getText().trim();
                String priceRange = priceRangeField.getText().trim();
    
                Double lowPrice = null;
                Double highPrice = null;
    
                if (!priceRange.isEmpty()) {
                    String[] prices = priceRange.split("-");
                    if (prices.length == 1) {
                        lowPrice = highPrice = Double.parseDouble(prices[0]);
                    } else if (prices.length == 2) {
                        if (!prices[0].isEmpty()) {
                            lowPrice = Double.parseDouble(prices[0]);
                        }
                        if (!prices[1].isEmpty()) {
                            highPrice = Double.parseDouble(prices[1]);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid price range format.");
                    }
                    if (lowPrice != null && lowPrice < 0) {
                        throw new IllegalArgumentException("Low price cannot be negative.");
                    }
                    if (highPrice != null && highPrice < 0) {
                        throw new IllegalArgumentException("High price cannot be negative.");
                    }
                    if (lowPrice != null && highPrice != null && lowPrice > highPrice) {
                        throw new IllegalArgumentException("Low price cannot be greater than high price.");
                    }
                }
    
                List<Investment> results = portfolio.search(symbol, keywords, lowPrice, highPrice);
    
                if (results.isEmpty()) {
                    resultsArea.setText("No matching investments found.");
                } else {
                    StringBuilder resultText = new StringBuilder();
                    for (Investment inv : results) {
                        resultText.append(inv.toString()).append("\n");
                    }
                    resultsArea.setText(resultText.toString());
                }
            } catch (NumberFormatException ex) {
                resultsArea.setText("Invalid number format: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                resultsArea.setText("Error: " + ex.getMessage());
            }
        });
    
        // Add main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }
    
}
