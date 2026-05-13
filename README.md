# 🔥 Habit Tracker — Java OOP Project

> Built with Java 17 + JavaFX + MySQL | OOP Course Project

---

## 📁 Project Structure

```
HabitTracker/
├── pom.xml                          ← Maven dependencies
├── src/main/
│   ├── java/
│   │   ├── module-info.java
│   │   └── com/habittracker/
│   │       ├── MainApp.java          ← Entry point
│   │       ├── db/
│   │       │   └── DatabaseConnection.java
│   │       ├── model/               ← OOP Models (Member 2)
│   │       │   ├── User.java
│   │       │   ├── Habit.java
│   │       │   ├── HabitLog.java
│   │       │   └── Streak.java
│   │       ├── dao/                 ← Database layer (Member 1)
│   │       │   ├── UserDAO.java
│   │       │   ├── HabitDAO.java
│   │       │   ├── HabitLogDAO.java
│   │       │   └── StreakDAO.java
│   │       ├── service/             ← Business Logic (Member 3)
│   │       │   ├── UserService.java
│   │       │   └── HabitService.java (contains Streak Engine)
│   │       ├── ui/                  ← JavaFX Screens (Members 4 & 5)
│   │       │   ├── LoginScreen.java
│   │       │   ├── DashboardScreen.java
│   │       │   ├── AddHabitScreen.java
│   │       │   └── StatsScreen.java
│   │       └── util/
│   │           └── PasswordUtil.java
│   └── resources/
│       └── schema.sql               ← Run this first!
```

---

## ⚙️ Setup Instructions

### Step 1: Database Setup
1. افتح MySQL Workbench أو أي MySQL client
2. شغّل الفايل ده:
   ```sql
   SOURCE src/main/resources/schema.sql;
   ```
   أو copy-paste محتواه في الـ SQL editor

### Step 2: Update Database Password
افتح `DatabaseConnection.java` وغيّر:
```java
private static final String PASSWORD = "your_password_here"; // ← غيّر ده
```

### Step 3: Run the Project
```bash
# باستخدام Maven
mvn javafx:run

# أو من IntelliJ: Run → MainApp
```

---

## 🏗️ OOP Concepts Used

| Concept | Where |
|---------|-------|
| **Encapsulation** | All model classes (User, Habit, Streak, HabitLog) |
| **Abstraction** | Service layer hides DB complexity from UI |
| **Inheritance** | JavaFX layout classes (VBox, HBox, BorderPane) |
| **Polymorphism** | Habit.Frequency enum, Optional return types |
| **Single Responsibility** | Each class has one clear job |
| **MVC Pattern** | Model (model/) + View (ui/) + Controller (service/) |

---

## 🔥 Streak Algorithm

```
If lastCompletedDate == yesterday  → currentStreak++
If lastCompletedDate == today      → no change (already counted)
If lastCompletedDate > 1 day ago  → reset to 1 (streak broken!)
If never completed                 → start at 1
longestStreak = max(currentStreak, longestStreak)
```

---

## 👥 Team Task Division

| Member | Task |
|--------|------|
| 1 | DB design + DAO classes + JDBC setup |
| 2 | Model classes (User, Habit, HabitLog, Streak) |
| 3 | Service layer + Streak engine algorithm |
| 4 | LoginScreen + DashboardScreen + AddHabitScreen |
| 5 | StatsScreen + UI styling + Alerts |
| 6 | Integration + Testing + README + Demo |

---

## 📦 Dependencies (pom.xml)
- JavaFX 21
- MySQL Connector/J 8.3
- Java 17+

---

## 🎨 UI Design
- Dark theme: `#1a1a2e` background
- Accent red: `#e94560`
- Success green: `#4ecca3`
- Streak gold: `#f59e0b`
