# ToDoList App

A clean and modern ToDoList application built for Android using Jetpack Compose, following MVVM and Clean Architecture principles, with local data persistence using Room.

## Features

This application provides the following core functionalities:

### Task List Screen
- Displays a comprehensive list of all tasks.
- Each task item shows its title, description, and completion status.
- Ability to mark a task as completed or incomplete via a checkbox.
- Ability to delete a task with an "Undo" option.
- Filter tasks by "All", "Active" (not completed), or "Completed" status.

### Add New Task
- Dedicated screen to input a new task's title and description.
- Save button to add the task to the list.

### Edit Task
- Ability to modify an existing task's title, description, and completion status.
- Changes are saved persistently.

### Data Persistence
- All tasks are stored locally using the Room Persistence Library.
- Data remains intact between app launches.

## Architecture

The application adheres to the MVVM (Model-View-ViewModel) architectural pattern combined with Clean Architecture principles. This layered approach promotes separation of concerns, testability, and maintainability.

### UI Layer (Presentation)
- Built entirely with Jetpack Compose for declarative UI.
- Consists of Composable Screens (e.g., TaskListScreen, AddEditTaskScreen) and ViewModels (TaskListViewModel, AddEditTaskViewModel).
- ViewModels hold UI state (using Kotlin Flow) and handle UI-specific logic and events.
- Compose Navigation manages screen transitions.
- Utilizes Material 3 Design System for a modern aesthetic.

### Domain Layer (Business Logic)
- Contains the core business rules and models.
- Written in pure Kotlin, independent of Android or specific data frameworks.
- Defines the Task domain model.
- Includes the TaskRepository interface (contract for data operations).
- Encapsulates business operations in Use Cases (e.g., GetTasksUseCase, UpsertTaskUseCase, DeleteTaskUseCase).

### Data Layer (Data Persistence)
- Responsible for providing data to the domain layer.
- Uses Room Persistence Library for local SQLite database operations.
- Includes TaskDao (Data Access Object) and TaskEntity (database schema).
- TaskRepositoryImpl implements the TaskRepository interface, bridging the domain layer with the Room database and handling data mapping between domain models (Task) and database entities (TaskEntity).

## Technologies Used

- Kotlin
- Jetpack Compose
- Hilt (Dagger Hilt) for Dependency Injection
- Room Persistence Library
- Kotlin Coroutines & Flow
- AndroidX Jetpack Libraries


## How to Run the Application
1. **Clone the Repository**
git clone https://github.com/aliyev-tural/TodoList.git
cd TodoList # Navigate into the cloned project directory

2. **Open in Android Studio**
- Launch Android Studio.
- Go to `File > Open`.
- Navigate to the `TodoList` project directory and open it.

3. **Sync Gradle**
- Android Studio will sync your project and download dependencies.
- Wait until "Build successful" appears.

4. **Troubleshooting Sync (if needed)**
- File > Sync Project with Gradle Files
- Build > Clean Project → then Build > Rebuild Project
- File > Invalidate Caches / Restart

5. **Run on Emulator or Device**
- Ensure an emulator or USB-connected Android device is available.
- Select your target device and press the Run button (`▶`).

6. **Explore the App**
- Use the `+` button to add tasks.
- Tap a task to edit.
- Use the checkboxes and filters to manage your task list.

