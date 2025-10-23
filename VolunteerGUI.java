import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * J.A.V.A (Just a Volunteer Away) - GUI Application
 * A Swing-based volunteer management system with modern UI
 */
public class VolunteerGUI extends JFrame {
    private User currentUser = null;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel splashPanel;
    
    // Color Theme
    private static final Color PRIMARY_COLOR = new Color(255, 140, 0);      // Orange
    private static final Color SECONDARY_COLOR = new Color(173, 216, 230);  // Light Blue
    private static final Color BACKGROUND_COLOR = new Color(255, 248, 234); // Beige
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color BUTTON_HOVER = new Color(255, 165, 0);       // Lighter Orange
    
    // Panel names
    private static final String SPLASH_PANEL = "Splash";
    private static final String LOGIN_PANEL = "Login";
    private static final String ELDERLY_PANEL = "Elderly";
    private static final String VOLUNTEER_PANEL = "Volunteer";
    private static final String ADMIN_PANEL = "Admin";
    
    
    public VolunteerGUI() {
        setTitle("J.A.V.A - Just a Volunteer Away");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Test database connection
        if (!DatabaseManager.testConnection()) {
            JOptionPane.showMessageDialog(this,
                "Cannot connect to database!\nPlease check:\n" +
                "1. MySQL is running\n" +
                "2. Database 'volunteer_app' exists\n" +
                "3. User credentials are correct",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Initialize CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Add panels
        mainPanel.add(createSplashPanel(), SPLASH_PANEL);
        mainPanel.add(createLoginPanel(), LOGIN_PANEL);
        mainPanel.add(createElderlyPanel(), ELDERLY_PANEL);
        mainPanel.add(createVolunteerPanel(), VOLUNTEER_PANEL);
        mainPanel.add(createAdminPanel(), ADMIN_PANEL);
        
        add(mainPanel);
        cardLayout.show(mainPanel, SPLASH_PANEL);
        
        // Start splash timer
        startSplashTimer();
    }
    
    /**
     * Creates the splash screen panel with customizable GIF size
     */
    private JPanel createSplashPanel() {
        splashPanel = new JPanel(new BorderLayout(15, 15));
        splashPanel.setBackground(BACKGROUND_COLOR);
        splashPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        
        // Title
        // JLabel titleLabel = new JLabel("J.A.V.A", SwingConstants.CENTER);
        // titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        // titleLabel.setForeground(PRIMARY_COLOR);
        // splashPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Loading animation panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);
        
        // Subtitle
        // JLabel subtitleLabel = new JLabel("Just a Volunteer Away", SwingConstants.CENTER);
        // subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        // subtitleLabel.setForeground(TEXT_COLOR);
        // centerPanel.add(subtitleLabel, BorderLayout.NORTH);
        
        // Loading GIF with custom size
        JLabel loadingLabel = new JLabel();
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        try {
            File gifFile = new File("splash.gif");
            if (gifFile.exists()) {
                ImageIcon originalIcon = new ImageIcon("splash.gif");
                // Use original size - no scaling
                loadingLabel.setIcon(originalIcon);
            } else {
                // Fallback text if GIF not found
                loadingLabel.setText("Loading Application...");
                loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
                loadingLabel.setForeground(PRIMARY_COLOR);
            }
        } catch (Exception e) {
            loadingLabel.setText("Loading Application...");
            loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
            loadingLabel.setForeground(PRIMARY_COLOR);
            System.err.println("Error loading splash GIF: " + e.getMessage());
        }
        
        centerPanel.add(loadingLabel, BorderLayout.CENTER);
        splashPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Version info
        JLabel versionLabel = new JLabel("Version 1.0 - Initializing...", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(108, 117, 125));
        splashPanel.add(versionLabel, BorderLayout.SOUTH);
        
        return splashPanel;
    }
    
    /**
     * Starts the splash screen timer and transitions to login
     */
    private void startSplashTimer() {
        // Use a Timer to transition from splash to login after delay
        javax.swing.Timer timer = new javax.swing.Timer(6000, e -> {
            cardLayout.show(mainPanel, LOGIN_PANEL);
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Creates a styled button with theme colors
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a secondary styled button (light blue theme)
     */
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250)); // Lighter blue
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
        });
        
        return button;
    }
    
    // ==================== LOGIN/REGISTRATION PANEL ====================
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Top panel with logo and welcome text
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints topGbc = new GridBagConstraints();
        topGbc.gridx = 0;
        topGbc.gridy = 0;
        topGbc.insets = new Insets(10, 10, 10, 10);
        
        // Load and display logo
        try {
            File logoFile = new File("logo.png");
            if (logoFile.exists()) {
                BufferedImage logoImg = ImageIO.read(logoFile);
                ImageIcon logoIcon = new ImageIcon(logoImg);
                JLabel logoLabel = new JLabel(logoIcon);
                topPanel.add(logoLabel, topGbc);
            }
        } catch (IOException e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        
        // Welcome header
        topGbc.gridy = 1;
        JLabel headerLabel = new JLabel("Welcome to J.A.V.A", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(headerLabel, topGbc);
        
        topGbc.gridy = 2;
        
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        /* 
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        */
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        centerPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        centerPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        centerPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        centerPanel.add(passwordField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createSecondaryButton("Register");
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            currentUser = DatabaseManager.loginUser(username, password);
            
            if (currentUser != null) {
                JOptionPane.showMessageDialog(this, "Welcome back, " + currentUser.getFirstName() + "!");
                if (currentUser.getRole().equals("ELDERLY")) {
                    refreshElderlyPanel();
                    cardLayout.show(mainPanel, ELDERLY_PANEL);
                } else if (currentUser.getRole().equals("ADMIN")) {
                    refreshAdminPanel();
                    cardLayout.show(mainPanel, ADMIN_PANEL);
                } else {
                    refreshVolunteerPanel();
                    cardLayout.show(mainPanel, VOLUNTEER_PANEL);
                }
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        registerButton.addActionListener(e -> showRegisterDialog());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        centerPanel.add(buttonPanel, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register New User", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Elderly (need help)", "Volunteer (provide help)"});
        
        // Style text fields
        JTextField[] fields = {usernameField, emailField, firstNameField, lastNameField, phoneField};
        for (JTextField field : fields) {
            field.setFont(new Font("Arial", Font.PLAIN, 13));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(firstNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lastNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(roleLabel, gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton submitButton = createStyledButton("Register");
        JButton cancelButton = createSecondaryButton("Cancel");
        
        submitButton.addActionListener(e -> {
            // Get values
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            // Validate empty fields
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || 
                firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "All fields are required!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(dialog, 
                    "Invalid email format!\nExample: user@example.com", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate password strength (minimum 8 characters)
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(dialog, 
                    "Password must be at least 8 characters long!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate phone number (basic check for digits)
            if (!phone.matches("^[0-9+\\-\\s()]{10,}$")) {
                JOptionPane.showMessageDialog(dialog, 
                    "Invalid phone number format!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create user if all validations pass
            String role = roleCombo.getSelectedIndex() == 0 ? "ELDERLY" : "VOLUNTEER";
            User newUser = new User(username, password, email, firstName, lastName, phone, role);
            
            if (DatabaseManager.registerUser(newUser)) {
                JOptionPane.showMessageDialog(dialog, "Registration successful! You can now log in.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Registration failed. Username or email might already exist.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    // ==================== ELDERLY PANEL ====================
    
    private JPanel elderlyPanel;
    private JPanel elderlyTasksContainer;
    
    private JPanel createElderlyPanel() {
        elderlyPanel = new JPanel(new BorderLayout(10, 10));
        elderlyPanel.setBackground(BACKGROUND_COLOR);
        elderlyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel headerLabel = new JLabel("Elderly User Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        elderlyPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center - Task cards container
        elderlyTasksContainer = new JPanel();
        elderlyTasksContainer.setLayout(new BoxLayout(elderlyTasksContainer, BoxLayout.Y_AXIS));
        elderlyTasksContainer.setBackground(BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(elderlyTasksContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        elderlyPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton createTaskButton = createStyledButton("Create New Task");
        JButton refreshButton = createSecondaryButton("Refresh Tasks");
        JButton confirmCompletionButton = createStyledButton("Confirm Completion");
        JButton removeVolunteerButton = createSecondaryButton("Remove Volunteer");
        JButton deleteTaskButton = createSecondaryButton("Delete Task");
        
        createTaskButton.addActionListener(e -> showCreateTaskDialog());
        refreshButton.addActionListener(e -> refreshElderlyPanel());
        confirmCompletionButton.addActionListener(e -> showElderlyConfirmDialog());
        removeVolunteerButton.addActionListener(e -> showRemoveVolunteerDialog());
        deleteTaskButton.addActionListener(e -> showDeleteTaskDialog());
        
        buttonPanel.add(createTaskButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(confirmCompletionButton);
        buttonPanel.add(removeVolunteerButton);
        buttonPanel.add(deleteTaskButton);
        
        elderlyPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return elderlyPanel;
    }
    
    private void refreshElderlyPanel() {
        if (currentUser == null) return;
        
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        elderlyTasksContainer.removeAll();
        
        if (tasks.isEmpty()) {
            JPanel emptyPanel = createEmptyStatePanel("No tasks yet", "Create your first task to get started!");
            elderlyTasksContainer.add(emptyPanel);
        } else {
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task, true);
                elderlyTasksContainer.add(taskCard);
                elderlyTasksContainer.add(Box.createVerticalStrut(15));
            }
        }
        
        elderlyTasksContainer.revalidate();
        elderlyTasksContainer.repaint();
    }
    
    /**
     * Creates a modern task card panel
     */
    private JPanel createTaskCard(Task task, boolean isElderlyView) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 230));
        
        // Left section - Main info
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 0, 3, 0);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        leftPanel.add(titleLabel, gbc);
        
        // ID
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel idLabelText = new JLabel("Task ID: ");
        idLabelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabelText.setForeground(new Color(73, 80, 87));
        leftPanel.add(idLabelText, gbc);
        
        gbc.gridx = 1;
        JLabel idValue = new JLabel(String.valueOf(task.getTaskId()));
        idValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idValue.setForeground(new Color(108, 117, 125));
        leftPanel.add(idValue, gbc);
        
        // Date
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLabel.setForeground(new Color(73, 80, 87));
        leftPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        JLabel dateValue = new JLabel(task.getScheduledDate());
        dateValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateValue.setForeground(new Color(33, 37, 41));
        leftPanel.add(dateValue, gbc);
        
        // Time
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel timeLabel = new JLabel("Time: ");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeLabel.setForeground(new Color(73, 80, 87));
        leftPanel.add(timeLabel, gbc);
        
        gbc.gridx = 1;
        JLabel timeValue = new JLabel(task.getScheduledTime());
        timeValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeValue.setForeground(new Color(33, 37, 41));
        leftPanel.add(timeValue, gbc);
        
        // Duration
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel durationLabel = new JLabel("Duration: ");
        durationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        durationLabel.setForeground(new Color(73, 80, 87));
        leftPanel.add(durationLabel, gbc);
        
        gbc.gridx = 1;
        JLabel durationValue = new JLabel(task.getEstimatedDuration() + " minutes");
        durationValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        durationValue.setForeground(new Color(33, 37, 41));
        leftPanel.add(durationValue, gbc);
        
        // Location
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel locationLabel = new JLabel("Location: ");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        locationLabel.setForeground(new Color(73, 80, 87));
        leftPanel.add(locationLabel, gbc);
        
        gbc.gridx = 1;
        JLabel locationValue = new JLabel(task.getLocation());
        locationValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationValue.setForeground(new Color(33, 37, 41));
        leftPanel.add(locationValue, gbc);
        
        // Requester (Elderly) info
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel requesterLabel = new JLabel("Requester: ");
        requesterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        requesterLabel.setForeground(new Color(73, 80, 87));
        leftPanel.add(requesterLabel, gbc);
        
        gbc.gridx = 1;
        String requesterName = DatabaseManager.getUserNameById(task.getRequesterId());
        JLabel requesterValue = new JLabel(requesterName + " (#" + task.getRequesterId() + ")");
        requesterValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        requesterValue.setForeground(new Color(33, 37, 41));
        leftPanel.add(requesterValue, gbc);
        
        // Volunteer info
        if (task.getVolunteerId() != null) {
            gbc.gridx = 0; gbc.gridy = 7;
            JLabel volunteerLabel = new JLabel("Volunteer: ");
            volunteerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            volunteerLabel.setForeground(new Color(73, 80, 87));
            leftPanel.add(volunteerLabel, gbc);
            
            gbc.gridx = 1;
            String volunteerName = DatabaseManager.getUserNameById(task.getVolunteerId());
            JLabel volunteerValue = new JLabel(volunteerName + " (#" + task.getVolunteerId() + ")");
            volunteerValue.setFont(new Font("Segoe UI", Font.BOLD, 13));
            volunteerValue.setForeground(new Color(40, 167, 69));
            leftPanel.add(volunteerValue, gbc);
        }
        
        card.add(leftPanel, BorderLayout.CENTER);
        
        // Right section - Status and badges
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Status badge
        JLabel statusBadge = createStatusBadge(task.getStatus());
        statusBadge.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        rightPanel.add(statusBadge);
        rightPanel.add(Box.createVerticalStrut(10));
        
        // Confirmation badges
        if (task.isVolunteerConfirmed()) {
            JLabel vConfirm = new JLabel("Volunteer Confirmed");
            vConfirm.setFont(new Font("Segoe UI", Font.BOLD, 11));
            vConfirm.setForeground(new Color(40, 167, 69));
            vConfirm.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            rightPanel.add(vConfirm);
            rightPanel.add(Box.createVerticalStrut(5));
        }
        
        if (task.isElderlyConfirmed()) {
            JLabel eConfirm = new JLabel("Elderly Confirmed");
            eConfirm.setFont(new Font("Segoe UI", Font.BOLD, 11));
            eConfirm.setForeground(new Color(40, 167, 69));
            eConfirm.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            rightPanel.add(eConfirm);
        }
        
        if (task.getStatus().equals("PENDING_ELDERLY_CONFIRMATION") || 
            task.getStatus().equals("PENDING_VOLUNTEER_CONFIRMATION")) {
            JLabel pendingLabel = new JLabel("Awaiting Confirmation");
            pendingLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            pendingLabel.setForeground(new Color(255, 193, 7));
            pendingLabel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
            rightPanel.add(pendingLabel);
        }
        
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    /**
     * Creates a status badge with appropriate color
     */
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(" " + status + " ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        
        switch (status) {
            case "AVAILABLE":
                badge.setBackground(new Color(23, 162, 184));
                badge.setForeground(Color.WHITE);
                break;
            case "ASSIGNED":
            case "IN_PROGRESS":
                badge.setBackground(new Color(255, 193, 7));
                badge.setForeground(Color.BLACK);
                break;
            case "COMPLETED":
                badge.setBackground(new Color(40, 167, 69));
                badge.setForeground(Color.WHITE);
                break;
            case "CANCELLED":
                badge.setBackground(new Color(108, 117, 125));
                badge.setForeground(Color.WHITE);
                break;
            case "PENDING_ELDERLY_CONFIRMATION":
            case "PENDING_VOLUNTEER_CONFIRMATION":
                badge.setBackground(new Color(255, 140, 0));
                badge.setForeground(Color.WHITE);
                break;
            default:
                badge.setBackground(new Color(108, 117, 125));
                badge.setForeground(Color.WHITE);
        }
        
        return badge;
    }
    
    /**
     * Creates an empty state panel
     */
    private JPanel createEmptyStatePanel(String title, String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(108, 117, 125));
        panel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(108, 117, 125));
        panel.add(messageLabel, gbc);
        
        return panel;
    }
    
    private void showCreateTaskDialog() {
        JDialog dialog = new JDialog(this, "Create New Task", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField titleField = new JTextField(25);
        JTextArea descArea = new JTextArea(3, 25);
        JScrollPane descScroll = new JScrollPane(descArea);
        JTextField locationField = new JTextField(25);
        JTextField dateField = new JTextField(25);
        JTextField timeField = new JTextField(25);
        JTextField durationField = new JTextField(25);
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(descScroll, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(timeField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Duration (minutes):"), gbc);
        gbc.gridx = 1;
        panel.add(durationField, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Create Task");
        JButton cancelButton = new JButton("Cancel");
        
        submitButton.addActionListener(e -> {
            try {
                int duration = Integer.parseInt(durationField.getText().trim());
                Task task = new Task(
                    titleField.getText().trim(),
                    descArea.getText().trim(),
                    currentUser.getUserId(),
                    locationField.getText().trim(),
                    dateField.getText().trim(),
                    timeField.getText().trim(),
                    duration
                );
                
                if (DatabaseManager.createTask(task)) {
                    JOptionPane.showMessageDialog(dialog, "Task created successfully! Task ID: " + task.getTaskId());
                    refreshElderlyPanel();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create task.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid duration value!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showRemoveVolunteerDialog() {
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        java.util.List<Task> assignedTasks = new java.util.ArrayList<>();
        
        for (Task task : tasks) {
            if (task.getVolunteerId() != null && 
                (task.getStatus().equals("ASSIGNED") || task.getStatus().equals("IN_PROGRESS"))) {
                assignedTasks.add(task);
            }
        }
        
        if (assignedTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You don't have any tasks with assigned volunteers.");
            return;
        }
        
        String[] taskOptions = new String[assignedTasks.size()];
        for (int i = 0; i < assignedTasks.size(); i++) {
            Task task = assignedTasks.get(i);
            taskOptions[i] = "ID: " + task.getTaskId() + " - " + task.getTitle() + 
                           " (Volunteer ID: " + task.getVolunteerId() + ")";
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select task to remove volunteer:",
            "Remove Volunteer",
            JOptionPane.QUESTION_MESSAGE,
            null,
            taskOptions,
            taskOptions[0]
        );
        
        if (selected != null) {
            int taskId = Integer.parseInt(selected.split(" ")[1]);
            if (DatabaseManager.reassignTask(taskId)) {
                JOptionPane.showMessageDialog(this, "Volunteer removed successfully!");
                refreshElderlyPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove volunteer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showElderlyConfirmDialog() {
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        java.util.List<Task> confirmableTasks = new java.util.ArrayList<>();
        
        // Filter tasks that can be confirmed
        for (Task task : tasks) {
            if (task.getVolunteerId() != null && 
                !task.getStatus().equals("COMPLETED") && 
                !task.isElderlyConfirmed()) {
                confirmableTasks.add(task);
            }
        }
        
        if (confirmableTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No tasks available for confirmation.\n" +
                "Tasks must be assigned to a volunteer and not yet fully completed.");
            return;
        }
        
        String[] taskOptions = new String[confirmableTasks.size()];
        for (int i = 0; i < confirmableTasks.size(); i++) {
            Task task = confirmableTasks.get(i);
            String statusInfo = task.getStatus();
            if (task.isVolunteerConfirmed()) {
                statusInfo += " [Volunteer confirmed - Awaiting your confirmation]";
            }
            taskOptions[i] = "ID: " + task.getTaskId() + " - " + task.getTitle() + 
                           " (" + statusInfo + ")";
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select task to confirm completion:",
            "Confirm Task Completion",
            JOptionPane.QUESTION_MESSAGE,
            null,
            taskOptions,
            taskOptions[0]
        );
        
        if (selected != null) {
            int taskId = Integer.parseInt(selected.split(" ")[1]);
            
            // Find the selected task
            Task selectedTask = null;
            for (Task task : confirmableTasks) {
                if (task.getTaskId() == taskId) {
                    selectedTask = task;
                    break;
                }
            }
            
            if (selectedTask == null) return;
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm that this task has been completed satisfactorily?",
                "Confirm Completion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseManager.elderlyConfirmTask(taskId, currentUser.getUserId())) {
                    String message = "You have confirmed this task as completed!\n";
                    if (selectedTask.isVolunteerConfirmed()) {
                        message += "The volunteer has also confirmed. Task is now COMPLETED!";
                    } else {
                        message += "Waiting for the volunteer to confirm completion.";
                    }
                    JOptionPane.showMessageDialog(this, message);
                    refreshElderlyPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to confirm task completion.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void showDeleteTaskDialog() {
        List<Task> tasks = DatabaseManager.getTasksByRequester(currentUser.getUserId());
        java.util.List<Task> deletableTasks = new java.util.ArrayList<>();
        
        // Only allow deleting tasks that are AVAILABLE or CANCELLED
        for (Task task : tasks) {
            if (task.getStatus().equals("AVAILABLE") || task.getStatus().equals("CANCELLED")) {
                deletableTasks.add(task);
            }
        }
        
        if (deletableTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "You don't have any tasks available for deletion.\n" +
                "Only AVAILABLE or CANCELLED tasks can be deleted.");
            return;
        }
        
        String[] taskOptions = new String[deletableTasks.size()];
        for (int i = 0; i < deletableTasks.size(); i++) {
            Task task = deletableTasks.get(i);
            taskOptions[i] = "ID: " + task.getTaskId() + " - " + task.getTitle() + 
                           " (Status: " + task.getStatus() + ")";
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select task to delete permanently:",
            "Delete Task",
            JOptionPane.WARNING_MESSAGE,
            null,
            taskOptions,
            taskOptions[0]
        );
        
        if (selected != null) {
            int taskId = Integer.parseInt(selected.split(" ")[1]);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this task?\nThis action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (DatabaseManager.deleteTask(taskId, currentUser.getUserId())) {
                    JOptionPane.showMessageDialog(this, "Task deleted successfully!");
                    refreshElderlyPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete task.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // ==================== VOLUNTEER PANEL ====================
    
    private JPanel volunteerPanel;
    private JPanel volunteerTasksContainer;
    private JLabel statsLabel;
    
    private JPanel createVolunteerPanel() {
        volunteerPanel = new JPanel(new BorderLayout(10, 10));
        volunteerPanel.setBackground(BACKGROUND_COLOR);
        volunteerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with stats
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel headerLabel = new JLabel("Volunteer Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.setBackground(BACKGROUND_COLOR);
        statsLabel = new JLabel("", SwingConstants.RIGHT);
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statsLabel.setForeground(TEXT_COLOR);
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topRightPanel.add(statsLabel, BorderLayout.CENTER);
        topRightPanel.add(logoutButton, BorderLayout.SOUTH);
        headerPanel.add(topRightPanel, BorderLayout.EAST);
        
        volunteerPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center - Task list with card layout
        volunteerTasksContainer = new JPanel();
        volunteerTasksContainer.setLayout(new BoxLayout(volunteerTasksContainer, BoxLayout.Y_AXIS));
        volunteerTasksContainer.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(volunteerTasksContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        volunteerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton availableTasksButton = createSecondaryButton("View Available Tasks");
        JButton acceptTaskButton = createStyledButton("Accept Task");
        JButton myTasksButton = createSecondaryButton("My Assigned Tasks");
        JButton updateStatusButton = createStyledButton("Update Status");
        JButton leaderboardButton = createSecondaryButton("Leaderboard");
        
        availableTasksButton.addActionListener(e -> showAvailableTasks());
        acceptTaskButton.addActionListener(e -> showAcceptTaskDialog());
        myTasksButton.addActionListener(e -> showMyAssignedTasks());
        updateStatusButton.addActionListener(e -> showUpdateStatusDialog());
        leaderboardButton.addActionListener(e -> showLeaderboard());
        
        buttonPanel.add(availableTasksButton);
        buttonPanel.add(acceptTaskButton);
        buttonPanel.add(myTasksButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(leaderboardButton);
        
        volunteerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return volunteerPanel;
    }
    
    private void refreshVolunteerPanel() {
        if (currentUser == null) return;
        
        // Update stats
        statsLabel.setText("<html>Points: " + currentUser.getPoints() + 
                          "<br>Tasks Completed: " + currentUser.getTasksCompleted() + "</html>");
        
        // Show assigned tasks by default
        showMyAssignedTasks();
    }
    
    private void showAvailableTasks() {
        List<Task> tasks = DatabaseManager.getAvailableTasks();
        
        volunteerTasksContainer.removeAll();
        
        if (tasks.isEmpty()) {
            JPanel emptyPanel = createEmptyStatePanel(
                "No Available Tasks",
                "There are no tasks available to accept at the moment. Please check back later!"
            );
            volunteerTasksContainer.add(emptyPanel);
        } else {
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task, true);
                volunteerTasksContainer.add(taskCard);
                volunteerTasksContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        volunteerTasksContainer.revalidate();
        volunteerTasksContainer.repaint();
    }
    
    private void showAcceptTaskDialog() {
        List<Task> tasks = DatabaseManager.getAvailableTasks();
        
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available tasks at the moment.");
            return;
        }
        
        String[] taskOptions = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskOptions[i] = "ID: " + task.getTaskId() + " - " + task.getTitle() + 
                           " (" + task.getScheduledDate() + " at " + task.getScheduledTime() + ")";
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select task to accept:",
            "Accept Task",
            JOptionPane.QUESTION_MESSAGE,
            null,
            taskOptions,
            taskOptions[0]
        );
        
        if (selected != null) {
            int taskId = Integer.parseInt(selected.split(" ")[1]);
            if (DatabaseManager.assignTask(taskId, currentUser.getUserId())) {
                JOptionPane.showMessageDialog(this, "Task accepted successfully!");
                showMyAssignedTasks();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to accept task. It may have been already assigned.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showMyAssignedTasks() {
        List<Task> tasks = DatabaseManager.getTasksByVolunteer(currentUser.getUserId());
        
        volunteerTasksContainer.removeAll();
        
        if (tasks.isEmpty()) {
            JPanel emptyPanel = createEmptyStatePanel(
                "No Assigned Tasks",
                "You don't have any assigned tasks yet. Check out the available tasks to get started!"
            );
            volunteerTasksContainer.add(emptyPanel);
        } else {
            for (Task task : tasks) {
                JPanel taskCard = createTaskCard(task, false);
                volunteerTasksContainer.add(taskCard);
                volunteerTasksContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        volunteerTasksContainer.revalidate();
        volunteerTasksContainer.repaint();
    }
    
    private void showUpdateStatusDialog() {
        List<Task> tasks = DatabaseManager.getTasksByVolunteer(currentUser.getUserId());
        
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You don't have any assigned tasks.");
            return;
        }
        
        // Filter tasks that can be updated
        List<Task> updatableTasks = new java.util.ArrayList<>();
        for (Task task : tasks) {
            if (!task.getStatus().equals("COMPLETED") && !task.isElderlyConfirmed()) {
                updatableTasks.add(task);
            }
        }
        
        if (updatableTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No tasks available to update.\n" +
                "Tasks confirmed by the elderly cannot be reverted.");
            return;
        }
        
        String[] taskOptions = new String[updatableTasks.size()];
        for (int i = 0; i < updatableTasks.size(); i++) {
            Task task = updatableTasks.get(i);
            String statusInfo = task.getStatus();
            if (task.isVolunteerConfirmed()) {
                statusInfo += " [You confirmed - Awaiting elderly]";
            }
            taskOptions[i] = "ID: " + task.getTaskId() + " - " + task.getTitle() + 
                           " (" + statusInfo + ")";
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select task to update:",
            "Update Task Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            taskOptions,
            taskOptions[0]
        );
        
        if (selected != null) {
            int taskId = Integer.parseInt(selected.split(" ")[1]);
            
            // Find the selected task
            Task selectedTask = null;
            for (Task task : updatableTasks) {
                if (task.getTaskId() == taskId) {
                    selectedTask = task;
                    break;
                }
            }
            
            if (selectedTask == null) return;
            
            // Build status options based on current state
            java.util.List<String> statusList = new java.util.ArrayList<>();
            if (!selectedTask.getStatus().equals("IN_PROGRESS")) {
                statusList.add("IN_PROGRESS");
            }
            if (!selectedTask.isVolunteerConfirmed()) {
                statusList.add("MARK AS COMPLETED");
            }
            
            if (statusList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task is already confirmed and cannot be changed.");
                return;
            }
            
            String[] statusOptions = statusList.toArray(new String[0]);
            String status = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                statusOptions[0]
            );
            
            if (status != null) {
                if (status.equals("MARK AS COMPLETED")) {
                    if (DatabaseManager.volunteerConfirmTask(taskId, currentUser.getUserId())) {
                        String message = "You have marked this task as completed!\n";
                        if (selectedTask.isElderlyConfirmed()) {
                            message += "The elderly has also confirmed. Task is now COMPLETED!\nPoints have been added to your account!";
                        } else {
                            message += "Waiting for the elderly to confirm completion.";
                        }
                        JOptionPane.showMessageDialog(this, message);
                        refreshVolunteerPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to confirm task completion.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (status.equals("IN_PROGRESS")) {
                    if (DatabaseManager.updateTaskStatus(taskId, "IN_PROGRESS")) {
                        JOptionPane.showMessageDialog(this, "Task status updated to IN_PROGRESS!");
                        refreshVolunteerPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update task status.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private void showLeaderboard() {
        List<User> leaderboard = DatabaseManager.getLeaderboard(10);
        
        JDialog dialog = new JDialog(this, "Volunteer Leaderboard", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        String[] columnNames = {"Rank", "Name", "Points", "Tasks Completed"};
        Object[][] data = new Object[leaderboard.size()][4];
        
        for (int i = 0; i < leaderboard.size(); i++) {
            User user = leaderboard.get(i);
            data[i][0] = i + 1;
            data[i][1] = user.getFirstName() + " " + user.getLastName();
            data[i][2] = user.getPoints();
            data[i][3] = user.getTasksCompleted();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    // ==================== ADMIN PANEL ====================
    
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel headerLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(PRIMARY_COLOR);
        
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());
        
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // System stats panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2), 
            "System Statistics",
            0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setBackground(Color.WHITE);
        statsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane statsScroll = new JScrollPane(statsArea);
        statsPanel.add(statsScroll, BorderLayout.CENTER);
        
        // Control buttons panel
        JPanel controlsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        controlsPanel.setBackground(BACKGROUND_COLOR);
        controlsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2), 
            "Admin Controls",
            0, 0, new Font("Arial", Font.BOLD, 14), TEXT_COLOR));
        
        JButton viewUsersBtn = createStyledButton("View All Users");
        JButton viewTasksBtn = createStyledButton("View All Tasks");
        JButton viewHistoryBtn = createSecondaryButton("View Task History");
        JButton manageUsersBtn = createStyledButton("Manage Users");
        JButton manageTasksBtn = createStyledButton("Manage Tasks");
        JButton refreshStatsBtn = createSecondaryButton("Refresh Statistics");
        
        viewUsersBtn.addActionListener(e -> showAllUsers());
        viewTasksBtn.addActionListener(e -> showAllTasks());
        viewHistoryBtn.addActionListener(e -> showTaskHistory());
        manageUsersBtn.addActionListener(e -> showManageUsers());
        manageTasksBtn.addActionListener(e -> showManageTasks());
        refreshStatsBtn.addActionListener(e -> {
            statsArea.setText(DatabaseManager.getSystemStats());
        });
        
        controlsPanel.add(viewUsersBtn);
        controlsPanel.add(viewTasksBtn);
        controlsPanel.add(viewHistoryBtn);
        controlsPanel.add(manageUsersBtn);
        controlsPanel.add(manageTasksBtn);
        controlsPanel.add(refreshStatsBtn);
        
        contentPanel.add(statsPanel);
        contentPanel.add(controlsPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void refreshAdminPanel() {
        // Panel will refresh when buttons are clicked
    }
    
    private void showAllUsers() {
        List<User> users = DatabaseManager.getAllUsers();
        
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.");
            return;
        }
        
        String[] columnNames = {"ID", "Username", "Name", "Email", "Role", "Points", "Tasks", "Status"};
        Object[][] data = new Object[users.size()][8];
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUserId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getFirstName() + " " + user.getLastName();
            data[i][3] = user.getEmail();
            data[i][4] = user.getRole();
            data[i][5] = user.getPoints();
            data[i][6] = user.getTasksCompleted();
            data[i][7] = user.isActive() ? "Active" : "Disabled";
        }
        
        JDialog dialog = new JDialog(this, "All Users", true);
        dialog.setSize(900, 500);
        dialog.setLocationRelativeTo(this);
        
        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showAllTasks() {
        List<Task> tasks = DatabaseManager.getAllTasks();
        
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks found.");
            return;
        }
        
        String[] columnNames = {"ID", "Title", "Status", "Requester", "Volunteer", "Date", "Duration"};
        Object[][] data = new Object[tasks.size()][7];
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            data[i][0] = task.getTaskId();
            data[i][1] = task.getTitle();
            data[i][2] = task.getStatus();
            
            // Get requester name
            String requesterName = DatabaseManager.getUserNameById(task.getRequesterId());
            data[i][3] = requesterName + " (#" + task.getRequesterId() + ")";
            
            // Get volunteer name
            if (task.getVolunteerId() != null) {
                String volunteerName = DatabaseManager.getUserNameById(task.getVolunteerId());
                data[i][4] = volunteerName + " (#" + task.getVolunteerId() + ")";
            } else {
                data[i][4] = "None";
            }
            
            data[i][5] = task.getScheduledDate();
            data[i][6] = task.getEstimatedDuration() + " min";
        }
        
        JDialog dialog = new JDialog(this, "All Tasks", true);
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(this);
        
        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showTaskHistory() {
        List<String> history = DatabaseManager.getTaskHistory();
        
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No task history found.");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Task History", true);
        dialog.setSize(900, 500);
        dialog.setLocationRelativeTo(this);
        
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        StringBuilder sb = new StringBuilder();
        sb.append("TASK HISTORY LOG\n");
        for (int i = 0; i < 100; i++) sb.append("=");
        sb.append("\n\n");
        
        for (String entry : history) {
            sb.append(entry).append("\n");
        }
        
        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(historyArea);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showManageUsers() {
        List<User> users = DatabaseManager.getAllUsers();
        
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.");
            return;
        }
        
        // Filter out admin users from management
        List<User> manageableUsers = new ArrayList<>();
        for (User user : users) {
            if (!user.getRole().equals("ADMIN")) {
                manageableUsers.add(user);
            }
        }
        
        if (manageableUsers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available to manage.");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Manage Users", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select a user to enable/disable their account:"));
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("User:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JComboBox<String> userCombo = new JComboBox<>();
        for (User user : manageableUsers) {
            String status = user.isActive() ? "Active" : "Disabled";
            userCombo.addItem(String.format("#%d - %s (%s) - %s - %s", 
                user.getUserId(), user.getUsername(), user.getRole(), 
                user.getFirstName() + " " + user.getLastName(), status));
        }
        centerPanel.add(userCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JButton disableButton = new JButton("Disable Account");
        JButton enableButton = new JButton("Enable Account");
        
        disableButton.addActionListener(e -> {
            int selectedIndex = userCombo.getSelectedIndex();
            if (selectedIndex >= 0) {
                User selectedUser = manageableUsers.get(selectedIndex);
                
                if (!selectedUser.isActive()) {
                    JOptionPane.showMessageDialog(dialog, "This account is already disabled!");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to disable account for:\n" +
                    selectedUser.getFirstName() + " " + selectedUser.getLastName() + 
                    " (" + selectedUser.getUsername() + ")?\n\n" +
                    "This will cancel all their active tasks!",
                    "Confirm Disable",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (DatabaseManager.toggleUserStatus(selectedUser.getUserId(), false)) {
                        JOptionPane.showMessageDialog(dialog, "Account disabled successfully!");
                        DatabaseManager.addTaskHistory(0, currentUser.getUserId(), "ADMIN_DISABLE_USER", 
                            null, "Disabled user #" + selectedUser.getUserId());
                        dialog.dispose();
                        showManageUsers(); // Refresh
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to disable account!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        enableButton.addActionListener(e -> {
            int selectedIndex = userCombo.getSelectedIndex();
            if (selectedIndex >= 0) {
                User selectedUser = manageableUsers.get(selectedIndex);
                
                if (selectedUser.isActive()) {
                    JOptionPane.showMessageDialog(dialog, "This account is already active!");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Enable account for:\n" +
                    selectedUser.getFirstName() + " " + selectedUser.getLastName() + 
                    " (" + selectedUser.getUsername() + ")?",
                    "Confirm Enable",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (DatabaseManager.toggleUserStatus(selectedUser.getUserId(), true)) {
                        JOptionPane.showMessageDialog(dialog, "Account enabled successfully!");
                        DatabaseManager.addTaskHistory(0, currentUser.getUserId(), "ADMIN_ENABLE_USER", 
                            null, "Enabled user #" + selectedUser.getUserId());
                        dialog.dispose();
                        showManageUsers(); // Refresh
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to enable account!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        actionPanel.add(disableButton);
        actionPanel.add(enableButton);
        centerPanel.add(actionPanel, gbc);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        
        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showManageTasks() {
        List<Task> tasks = DatabaseManager.getAllTasks();
        
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks found.");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Manage Tasks", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select a task to delete:"));
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Task:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JComboBox<String> taskCombo = new JComboBox<>();
        for (Task task : tasks) {
            taskCombo.addItem(String.format("#%d - %s [%s] - %s", 
                task.getTaskId(), task.getTitle(), task.getStatus(), task.getScheduledDate()));
        }
        centerPanel.add(taskCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setBackground(new Color(255, 100, 100));
        
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskCombo.getSelectedIndex();
            if (selectedIndex >= 0) {
                Task selectedTask = tasks.get(selectedIndex);
                
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to permanently delete this task?\n\n" +
                    "Task: " + selectedTask.getTitle() + "\n" +
                    "Status: " + selectedTask.getStatus() + "\n" +
                    "Date: " + selectedTask.getScheduledDate() + "\n\n" +
                    "This action cannot be undone!",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (DatabaseManager.adminDeleteTask(selectedTask.getTaskId())) {
                        JOptionPane.showMessageDialog(dialog, "Task deleted successfully!");
                        DatabaseManager.addTaskHistory(selectedTask.getTaskId(), currentUser.getUserId(), 
                            "ADMIN_DELETE", selectedTask.getStatus(), "DELETED");
                        dialog.dispose();
                        showManageTasks(); // Refresh
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to delete task!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        centerPanel.add(deleteButton, gbc);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        
        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    // ==================== UTILITY METHODS ====================
    
    private void logout() {
        currentUser = null;
        cardLayout.show(mainPanel, LOGIN_PANEL);
        JOptionPane.showMessageDialog(this, "Logged out successfully!");
    }
    
    // ==================== MAIN ====================
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            VolunteerGUI gui = new VolunteerGUI();
            gui.setVisible(true);
        });
    }
}
