Summary
The WorldSimulator class is a Java class that simulates a world with a target and two towers. It uses Kafka to produce and consume messages related to the position of the target and towers.

Example Usage
WorldSimulator simulator = new WorldSimulator();
simulator.startSimulation();

This code creates an instance of the WorldSimulator class and starts the simulation by calling the startSimulation() method.


Code Analysis,

Main functionalities
Creates a graphical user interface (GUI) for the world simulation.
Uses Kafka to produce and consume messages related to the position of the target and towers.
Simulates the movement of the target on the GUI.
 
Methods
startSimulation(): Creates and shows the GUI, initializes the Kafka consumer, and starts a new thread to run the Kafka consumer.
createAndShowGUI(): Creates the main frame and panels for the GUI, including a scene panel for drawing the world, and control buttons for playing and stopping the simulation.
drawCoordinatePlane(Graphics g): Draws a coordinate plane on the scene panel.
drawTarget(Graphics g): Draws the target on the scene panel.
drawTowers(Graphics g): Draws the towers on the scene panel and produces Kafka messages for their positions.
simulateTargetMovement(): Simulates the movement of the target by updating its position randomly within a certain range at regular intervals using a timer.
 
Fields
frame: The main frame of the GUI.
mainPanel: The main panel of the GUI.
controlPanel: The control panel of the GUI.
targetX: The x-coordinate of the target's position.
targetY: The y-coordinate of the target's position.
isPlaying: A boolean flag indicating whether the simulation is currently playing.
kafkaProducer: An instance of the KafkaProducer class for producing Kafka messages.
