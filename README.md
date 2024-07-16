# Battleship Game

## Description

The Battleship Game is a strategic and interactive GUI-based game where players challenge computer-controlled opponents. It offers varying levels of difficulty and leverages advanced gaming mechanics to provide an immersive experience. This game is developed using JavaFX, showcasing dynamic imagery and sound effects.

## Prerequisites

Before you run this application, you will need:

- Java Development Kit (JDK) 21 or higher
- JavaFX SDK 22.0.1

## Setup Instructions

### 1. Install JDK

Ensure that Java Development Kit (JDK) 21 or higher is installed on your machine. You can download the latest version from [Oracle's official website](https://www.oracle.com/java/technologies/javase-downloads.html).

### 2. Download JavaFX

Download the JavaFX SDK from [OpenJFX](https://openjfx.io). Make sure you download version 22.0.1 or compatible with your JDK version.

### 3. Clone the Repository

Clone this repository to your local machine using Git:

```bash
git clone https://your-repository-url.git
cd Battleship-Game
```

### 4. Compile the Game

Navigate to the project directory and compile the game using:
```bash
javac --module-path /Users/ricardogonzalez/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.media -d bin src/BattleLines.java
```

### 5. Run the Game
```bash
java --module-path /Users/ricardogonzalez/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.media -cp bin BattleLines
```

