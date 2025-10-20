@startuml volunteer_app_database

!define TABLE(x) class x << (T,#FFAAAA) >>
!define PK(x) <u>x</u>
!define FK(x) <i>x</i>

' Database schema
package "volunteer_app Database" {
    
    TABLE(users) {
        PK(user_id) : INT [AUTO_INCREMENT]
        --
        username : VARCHAR(50) [UNIQUE]
        password : VARCHAR(255)
        email : VARCHAR(100)
        first_name : VARCHAR(50)
        last_name : VARCHAR(50)
        phone_number : VARCHAR(20)
        role : ENUM('ELDERLY', 'VOLUNTEER', 'ADMIN')
        is_active : BOOLEAN [DEFAULT TRUE]
    }
    
    TABLE(tasks) {
        PK(task_id) : INT [AUTO_INCREMENT]
        --
        title : VARCHAR(100)
        description : TEXT
        FK(requester_id) : INT
        FK(volunteer_id) : INT [NULL]
        status : ENUM
        location : VARCHAR(255)
        scheduled_date : DATE
        scheduled_time : TIME
        estimated_duration : INT (minutes)
        volunteer_confirmed : BOOLEAN [DEFAULT FALSE]
        elderly_confirmed : BOOLEAN [DEFAULT FALSE]
        previous_volunteer_id : INT [NULL]
        reassignment_reason : TEXT [NULL]
    }
    
    TABLE(user_points) {
        PK(FK(user_id)) : INT
        --
        points : INT [DEFAULT 0]
        level : INT [DEFAULT 1]
        user_rank : VARCHAR(50) [DEFAULT 'Newcomer']
        tasks_completed : INT [DEFAULT 0]
        hours_volunteered : INT [DEFAULT 0]
    }
    
    TABLE(task_history) {
        PK(history_id) : INT [AUTO_INCREMENT]
        --
        FK(task_id) : INT
        FK(changed_by_id) : INT
        action_type : VARCHAR(50)
        previous_status : VARCHAR(50) [NULL]
        new_status : VARCHAR(50) [NULL]
        changed_at : TIMESTAMP [DEFAULT CURRENT_TIMESTAMP]
    }
    
    note right of tasks
        **Task Status Values:**
        - AVAILABLE
        - ASSIGNED
        - IN_PROGRESS
        - PENDING_ELDERLY_CONFIRMATION
        - PENDING_VOLUNTEER_CONFIRMATION
        - COMPLETED
        - CANCELLED
        
        **Points System:**
        10 points per 30 minutes
    end note
    
    note right of users
        **User Roles:**
        - ELDERLY: Creates tasks
        - VOLUNTEER: Accepts tasks
        - ADMIN: System management
        
        **is_active:**
        Admin can disable accounts
    end note
}

' Relationships
users "1" -- "0..*" tasks : requester_id\n(creates tasks)
users "1" -- "0..*" tasks : volunteer_id\n(assigned to)
users "1" -- "0..1" user_points : (volunteers only)
users "1" -- "0..*" task_history : changed_by_id
tasks "1" -- "0..*" task_history : logs changes

' Database connection info
note bottom of "volunteer_app Database"
    **Connection Details:**
    URL: jdbc:mysql://localhost:3306/volunteer_app
    User: root
    Password: root
    Driver: com.mysql.cj.jdbc.Driver
end note

@enduml