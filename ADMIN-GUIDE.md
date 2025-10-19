# Admin Panel Guide

## Overview

The J.A.V.A application now includes a comprehensive **Admin Panel** that allows system administrators to monitor and manage the entire volunteer platform.

## Admin Login

**Username:** `admin`  
**Password:** `admin123`

⚠️ **IMPORTANT:** Change the default admin password after first login for security!

## Admin Panel Features

### 1. System Statistics Dashboard

The admin dashboard displays real-time statistics:

- **Active Users** - Total number of active user accounts
- **Active Volunteers** - Number of active volunteer users
- **Active Elderly** - Number of active elderly users
- **Total Tasks** - All tasks in the system
- **Available Tasks** - Tasks waiting for volunteers
- **Completed Tasks** - Successfully completed tasks
- **In Progress Tasks** - Tasks currently being worked on

Click **"Refresh Statistics"** to update the dashboard with latest data.

### 2. View All Users

**Button:** "View All Users"

Displays a comprehensive table showing:
- User ID
- Username
- Full Name
- Email Address
- Role (ELDERLY, VOLUNTEER, or ADMIN)
- Points (for volunteers)
- Tasks Completed
- Account Status (Active/Disabled)

This gives admins a complete overview of all registered users in the system.

### 3. View All Tasks

**Button:** "View All Tasks"

Shows a detailed table of all tasks:
- Task ID
- Title
- Status
- Requester (User ID)
- Volunteer (User ID or "None")
- Scheduled Date
- Duration (in minutes)

Useful for monitoring task distribution and identifying bottlenecks.

### 4. View Task History

**Button:** "View Task History"

Displays a chronological log of all task-related activities:
- Timestamp
- User who made the change
- User role
- Task ID and title
- Action type (CREATED, ASSIGNED, REASSIGNED, COMPLETED, etc.)
- Status transitions

**Example Log Entry:**
```
[2025-10-20 14:23:45] jane_smith (VOLUNTEER) - Task #5 'Grocery Shopping': ASSIGNED AVAILABLE -> ASSIGNED
```

This provides complete audit trail for:
- Task acceptance
- Task completion
- Status changes
- Confirmations
- Reassignments
- Admin actions

### 5. Manage Users

**Button:** "Manage Users"

#### Disable User Account

**What it does:**
- Sets the user account status to "Disabled"
- Prevents user from logging in
- Automatically cancels all their active tasks:
  - Tasks they requested (as elderly) → Status: CANCELLED
  - Tasks they're assigned to (as volunteer) → Removed from task, status: AVAILABLE
- Does NOT delete the user or their history

**When to use:**
- User violates terms of service
- User requests account suspension
- Spam/fake accounts
- Investigation of suspicious activity

**How to use:**
1. Click "Manage Users"
2. Select user from dropdown
3. Click "Disable Account"
4. Confirm the action
5. System will automatically handle all active tasks

#### Enable User Account

**What it does:**
- Reactivates a disabled account
- User can log in again
- User can create/accept tasks again

**When to use:**
- Investigation concluded
- User requests reactivation
- False positive

**Note:** Admins cannot disable other admin accounts.

### 6. Manage Tasks

**Button:** "Manage Tasks"

#### Delete Task Permanently

**What it does:**
- Permanently removes a task from the database
- Also deletes all task history entries related to this task
- **CANNOT BE UNDONE!**

**When to use:**
- Duplicate tasks
- Test data cleanup
- Inappropriate content
- Spam tasks
- User requests deletion (GDPR compliance)

**How to use:**
1. Click "Manage Tasks"
2. Select task from dropdown (shows ID, title, status, date)
3. Click "Delete Task" (red button)
4. Confirm deletion
5. Task is permanently removed

**Warning:** This bypasses normal deletion rules:
- Normal users can only delete AVAILABLE or CANCELLED tasks
- Admins can delete ANY task regardless of status
- Use with caution!

## Admin Capabilities Summary

| Feature | Regular User | Admin |
|---------|-------------|-------|
| View own tasks | ✅ | ✅ |
| View ALL tasks | ❌ | ✅ |
| View ALL users | ❌ | ✅ |
| View task history | ❌ | ✅ |
| Disable accounts | ❌ | ✅ |
| Delete any task | ❌ | ✅ |
| View system stats | ❌ | ✅ |

## Database Changes

### Schema Updates

#### Users Table
```sql
role VARCHAR(20) NOT NULL -- Now includes 'ADMIN' role
```

#### New Admin User
```sql
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES ('admin', 'admin123', 'admin@volunteer.com', 'System', 'Administrator', '555-0000', 'ADMIN', '1990-01-01');
```

### New DatabaseManager Methods

1. **`getAllUsers()`** - Retrieves all users with their points and status
2. **`getAllTasks()`** - Retrieves all tasks regardless of status
3. **`toggleUserStatus(userId, isActive)`** - Enable/disable user accounts
4. **`cancelUserActiveTasks(userId)`** - Automatically cancel tasks when user disabled
5. **`adminDeleteTask(taskId)`** - Force delete any task
6. **`getTaskHistory()`** - Retrieve task activity log
7. **`addTaskHistory(taskId, userId, actionType, previousStatus, newStatus)`** - Log task changes
8. **`getSystemStats()`** - Get platform statistics

## Security Considerations

### Best Practices

1. **Change Default Password**
   - Immediately change admin password from default
   - Use strong password (min 12 characters, mixed case, numbers, symbols)

2. **Account Disabling vs Deletion**
   - Use "Disable" instead of deleting user accounts
   - Preserves audit trail
   - Can be reversed if needed
   - Complies with data retention policies

3. **Task Deletion**
   - Only delete tasks when absolutely necessary
   - Consider cancelling instead of deleting
   - Deleted tasks lose all history

4. **Activity Logging**
   - All admin actions are logged in task_history table
   - Includes admin user ID for accountability
   - Cannot be deleted by admins (database-level protection)

5. **Access Control**
   - Only users with role='ADMIN' can access admin panel
   - Login checks for active status
   - Regular users cannot see admin features

## Task History Log Format

The task history table tracks all significant events:

```sql
CREATE TABLE task_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT NOT NULL,
    changed_by_id INT NOT NULL,
    action_type VARCHAR(50), -- 'CREATED', 'ASSIGNED', 'REASSIGNED', 
                             -- 'COMPLETED', 'CANCELLED', 
                             -- 'ADMIN_DELETE', 'ADMIN_DISABLE_USER', 
                             -- 'ADMIN_ENABLE_USER'
    previous_status VARCHAR(30),
    new_status VARCHAR(30),
    previous_volunteer_id INT,
    new_volunteer_id INT,
    reassignment_reason TEXT,
    notes TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Admin Action Types

- **`ADMIN_DELETE`** - Admin deleted a task
- **`ADMIN_DISABLE_USER`** - Admin disabled user account
- **`ADMIN_ENABLE_USER`** - Admin enabled user account

## Common Admin Workflows

### Workflow 1: Handling Spam Account

1. Login as admin
2. Click "View All Users"
3. Identify suspicious account
4. Click "Manage Users"
5. Select the spam account
6. Click "Disable Account"
7. Confirm action
8. System automatically cancels their tasks

### Workflow 2: Cleaning Test Data

1. Login as admin
2. Click "View All Tasks"
3. Identify test tasks
4. Click "Manage Tasks"
5. Select each test task
6. Click "Delete Task"
7. Confirm deletion

### Workflow 3: Investigating Task Issue

1. Login as admin
2. Click "View Task History"
3. Search for task ID or user
4. Review timeline of events
5. Identify the problem
6. Take appropriate action (disable user, delete task, etc.)

### Workflow 4: Platform Monitoring

1. Login as admin
2. Dashboard shows real-time stats
3. Click "Refresh Statistics" periodically
4. Monitor for unusual patterns:
   - Sudden spike in tasks
   - Low volunteer participation
   - High cancellation rate
5. Investigate using "View All Users" and "View All Tasks"

## Troubleshooting

### Issue: Cannot login as admin

**Solution:**
- Verify username is exactly `admin` (case-sensitive)
- Verify password is `admin123`
- Check database: `SELECT * FROM users WHERE role = 'ADMIN'`
- Ensure `is_active = TRUE` in database

### Issue: Admin panel buttons not working

**Solution:**
- Check database connection
- Verify MySQL server is running
- Check console for error messages
- Ensure user has ADMIN role in database

### Issue: Task history is empty

**Solution:**
- Task history only shows last 100 entries
- History is only created when changes are made
- Check if `task_history` table exists in database
- Verify foreign key constraints are working

### Issue: Cannot disable a user

**Solution:**
- Cannot disable admin users (by design)
- User must exist in database
- Check for database connection errors
- Verify user is not already disabled

## Admin Panel UI

### Layout

```
+--------------------------------------------------+
|  Admin Dashboard                      [Logout]  |
+--------------------------------------------------+
|                                                  |
|  System Statistics                               |
|  +--------------------------------------------+  |
|  | Active Users: 25                          |  |
|  | Active Volunteers: 15                     |  |
|  | Active Elderly: 10                        |  |
|  | Total Tasks: 100                          |  |
|  | Available Tasks: 20                       |  |
|  | Completed Tasks: 60                       |  |
|  | In Progress Tasks: 15                     |  |
|  +--------------------------------------------+  |
|                                                  |
|  Admin Controls                                  |
|  +--------------------+--------------------+     |
|  | View All Users     | View All Tasks     |     |
|  +--------------------+--------------------+     |
|  | View Task History  | Manage Users       |     |
|  +--------------------+--------------------+     |
|  | Manage Tasks       | Refresh Statistics |     |
|  +--------------------+--------------------+     |
+--------------------------------------------------+
```

## Compliance & Legal

### Data Protection

- Admin can view all user data (necessary for platform management)
- User deletion should preserve anonymized statistics
- GDPR: Users have right to be forgotten - use task deletion feature
- Admins should maintain confidentiality of user information

### Audit Trail

- All admin actions are logged
- Logs cannot be deleted (database-level protection)
- Includes timestamp, admin ID, and action details
- Useful for compliance audits

## Future Enhancements

Potential admin features for future versions:

- [ ] Export data to CSV/Excel
- [ ] Advanced filtering and search
- [ ] User statistics and reports
- [ ] Bulk operations (disable multiple users)
- [ ] Email notifications to users
- [ ] Password reset functionality
- [ ] Admin activity dashboard
- [ ] Custom report generation
- [ ] Scheduled task cleanup
- [ ] User feedback/rating system management

## Summary

The admin panel provides comprehensive control over the J.A.V.A platform:

✅ **Monitor** - Real-time statistics and activity logs  
✅ **Manage** - User accounts and tasks  
✅ **Investigate** - Complete task history and audit trail  
✅ **Protect** - Disable spam accounts and remove inappropriate content  
✅ **Maintain** - Clean test data and manage platform health

Use these powerful tools responsibly to ensure a safe, efficient, and trustworthy volunteer platform!
