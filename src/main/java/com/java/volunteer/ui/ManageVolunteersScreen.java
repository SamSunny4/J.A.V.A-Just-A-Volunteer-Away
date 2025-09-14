package com.java.volunteer.ui;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;
import com.java.volunteer.service.UserPointsService;

/**
 * Screen for elderly users to manage task volunteers, including removing volunteers and requesting reassignments
 */
public class ManageVolunteersScreen extends BaseScreen {
    private final TaskService taskService;
    private final UserPointsService userPointsService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public ManageVolunteersScreen(User currentUser, TaskService taskService, UserPointsService userPointsService) {
        super(currentUser);
        this.taskService = taskService;
        this.userPointsService = userPointsService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("Manage Task Volunteers");
        
        // Get all tasks created by the current user
        List<Task> tasks = taskService.getTasksByRequesterId(currentUser.getUserId());
        
        // Filter for tasks that have volunteers assigned
        List<Task> assignedTasks = tasks.stream()
            .filter(task -> task.getVolunteerId() != null && 
                  (task.getStatus() == Task.Status.ASSIGNED || task.getStatus() == Task.Status.IN_PROGRESS))
            .collect(java.util.stream.Collectors.toList());
        
        if (assignedTasks.isEmpty()) {
            showMessage("You don't have any tasks with assigned volunteers to manage.");
            return new MainMenuScreen(currentUser, null, taskService, userPointsService);
        }
        
        System.out.println("\nYour tasks with assigned volunteers:");
        System.out.println(String.format("%-5s %-30s %-15s %-20s %-10s", 
            "ID", "Title", "Date", "Volunteer", "Status"));
        System.out.println("--------------------------------------------------------------------------------");
        
        for (Task task : assignedTasks) {
            System.out.println(String.format("%-5d %-30s %-15s %-20s %-10s",
                task.getTaskId(),
                truncateString(task.getTitle(), 28),
                task.getScheduledDate().toString(),
                task.getVolunteer().getFullName(),
                task.getStatus()));
        }
        
        int taskId = getNumericInput("\nEnter task ID to manage volunteer (0 to go back)");
        if (taskId == 0) {
            return new MainMenuScreen(currentUser, null, taskService, userPointsService);
        }
        
        // Find the selected task
        Task selectedTask = assignedTasks.stream()
            .filter(task -> task.getTaskId() == taskId)
            .findFirst()
            .orElse(null);
        
        if (selectedTask == null) {
            showMessage("Invalid task ID selected.");
            return this;
        }
        
        return manageTaskVolunteer(selectedTask);
    }
    
    private BaseScreen manageTaskVolunteer(Task task) {
        showHeader("Manage Volunteer for Task: " + task.getTitle());
        
        System.out.println("\nTask Details:");
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Date & Time: " + task.getScheduledDate() + " " + task.getScheduledTime());
        System.out.println("Status: " + task.getStatus());
        
        System.out.println("\nVolunteer Details:");
        System.out.println("Name: " + task.getVolunteer().getFullName());
        System.out.println("Contact: " + task.getVolunteer().getPhoneNumber());
        
        int choice = showMenu(
            "Remove Volunteer and Add Task Back to Available Tasks",
            "View Volunteer Profile",
            "Back to Task List"
        );
        
        switch (choice) {
            case 1:
                return removeVolunteer(task);
            case 2:
                // This would show the volunteer profile, but for now just show a message
                showMessage("Volunteer profile information:\nName: " + task.getVolunteer().getFullName() + 
                          "\nEmail: " + task.getVolunteer().getEmail() + 
                          "\nPhone: " + task.getVolunteer().getPhoneNumber());
                return manageTaskVolunteer(task);
            case 3:
            case 0:
                return this.display();
            default:
                return manageTaskVolunteer(task);
        }
    }
    
    private BaseScreen removeVolunteer(Task task) {
        showHeader("Remove Volunteer");
        
        System.out.println("\nYou are about to remove the volunteer '" + 
                          task.getVolunteer().getFullName() + "' from task: " + task.getTitle());
        System.out.println("\nPlease provide a reason for removing this volunteer:");
        
        Scanner scanner = new Scanner(System.in);
        String reason = scanner.nextLine().trim();
        
        if (reason.isEmpty()) {
            System.out.println("A reason is required for removing a volunteer.");
            return manageTaskVolunteer(task);
        }
        
        if (confirm("Are you sure you want to remove this volunteer and add the task back to available tasks?")) {
            // Use the task service to reassign the task
            Task updatedTask = taskService.reassignTask(task.getTaskId(), currentUser.getUserId(), reason);
            
            if (updatedTask != null) {
                // Update volunteer points
                userPointsService.addTaskReassignment(task.getVolunteerId());
                
                showMessage("Volunteer has been removed and the task has been added back to the available tasks.");
            } else {
                showMessage("Failed to remove volunteer. Please try again.");
            }
        } else {
            showMessage("Volunteer removal cancelled.");
        }
        
        return this.display();
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}