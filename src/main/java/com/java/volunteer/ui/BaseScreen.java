package com.java.volunteer.ui;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.model.User;

/**
 * Base class for console UI screens
 */
public abstract class BaseScreen {
    protected static final Logger logger = LoggerFactory.getLogger(BaseScreen.class);
    protected static final Scanner scanner = new Scanner(System.in);
    protected User currentUser;
    
    public BaseScreen(User currentUser) {
        this.currentUser = currentUser;
    }
    
    /**
     * Display the screen
     * @return the next screen to navigate to, or null to exit
     */
    public abstract BaseScreen display();
    
    /**
     * Show the screen header with title
     * @param title the screen title
     */
    protected void showHeader(String title) {
        System.out.println("\n=================================================");
        System.out.println("  " + title);
        System.out.println("=================================================");
        
        if (currentUser != null) {
            System.out.println("  Logged in as: " + currentUser.getUsername() + 
                             " (" + currentUser.getRoleName() + ")");
            System.out.println("=================================================");
        }
    }
    
    /**
     * Show a menu and get the user's selection
     * @param options the menu options
     * @return the selected option index (1-based) or 0 for invalid selection
     */
    protected int showMenu(String... options) {
        System.out.println();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back");
        System.out.print("\nEnter your choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 0 && choice <= options.length) {
                return choice;
            } else {
                System.out.println("Invalid choice. Please try again.");
                return showMenu(options);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return showMenu(options);
        }
    }
    
    /**
     * Get input from the user
     * @param prompt the input prompt
     * @return the user input
     */
    protected String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Get a non-empty input from the user
     * @param prompt the input prompt
     * @return the user input (non-empty)
     */
    protected String getRequiredInput(String prompt) {
        String input = getInput(prompt);
        while (input.isEmpty()) {
            System.out.println("This field is required.");
            input = getInput(prompt);
        }
        return input;
    }
    
    /**
     * Get a numeric input from the user
     * @param prompt the input prompt
     * @return the numeric value
     */
    protected int getNumericInput(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(getRequiredInput(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Ask the user for confirmation
     * @param prompt the confirmation prompt
     * @return true if confirmed, false otherwise
     */
    protected boolean confirm(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
    
    /**
     * Show a message and wait for user acknowledgment
     * @param message the message to display
     */
    protected void showMessage(String message) {
        System.out.println("\n" + message);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}