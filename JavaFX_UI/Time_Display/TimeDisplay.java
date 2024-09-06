   private void setTime() {
        // Initialize the clock
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Set initial time
        timeLabel.setText(LocalTime.now().format(timeFormat));

        // Create a timeline to update the clock every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    Platform.runLater(() -> timeLabel.setText(LocalTime.now().format(timeFormat)));
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }