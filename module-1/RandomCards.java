/*
 * Name:       Kristian Wareing
 * Date:       October 25, 2026
 * Course:     CSD-420 Advanced Java Programming
 * Assignment: Module 1.2 - Random Card Display (JavaFX)
 *
 * Purpose:
 *   Displays four cards chosen at random from a standard 52-card deck.
 *   The card images (1.png through 52.png) are loaded from a "cards"
 *   subdirectory next to this program. A Refresh button below the cards
 *   deals a new hand of four cards that do not repeat any card from the
 *   hand currently shown. Lambda expressions are used for the button's
 *   event handler and for building/updating the card images. Missing or
 *   unreadable images are reported to the console instead of failing silently.
 *
 * Attribution:
 *   Author: Kristian Wareing. Card images (1.png - 52.png) are from the
 *   cards.zip file provided with the CSD-420 Module 1 assignment.
 *
 * How to run (Windows Command Prompt, from the folder that contains
 * this file and the "cards" folder):
 *   javac --module-path %PATH_TO_FX% --add-modules javafx.controls RandomCards.java
 *   java  --module-path %PATH_TO_FX% --add-modules javafx.controls RandomCards
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RandomCards extends Application {

    /** Number of cards in a standard deck (images 1.png - 52.png). */
    private static final int DECK_SIZE = 52;

    /** Number of cards shown at one time. */
    private static final int HAND_SIZE = 4;

    /** Folder that holds the card images, relative to the working directory. */
    private static final String CARD_DIR = "cards";

    /** The four image views that display the current hand. */
    private final List<ImageView> cardViews = new ArrayList<>();

    /** Card numbers currently on screen, used so a refresh shows different cards. */
    private List<Integer> currentHand = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Warn early if the program is not run from the folder that holds "cards".
        File cardFolder = new File(CARD_DIR);
        if (!cardFolder.isDirectory()) {
            System.err.println("Error: The \"" + CARD_DIR + "\" folder was not found at "
                    + cardFolder.getAbsolutePath()
                    + ". Run the program from the folder that contains it.");
        }

        // Create one ImageView per card slot (lambda inside forEach).
        IntStream.range(0, HAND_SIZE).forEach(i -> cardViews.add(new ImageView()));

        // Row of cards across the top.
        HBox cardRow = new HBox(10);
        cardRow.setAlignment(Pos.CENTER);
        cardRow.getChildren().addAll(cardViews);

        // Refresh button below the cards; the event handler is a lambda.
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> dealHand());

        // Stack the card row above the button.
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(cardRow, refreshButton);

        dealHand(); // show the first hand

        primaryStage.setTitle("Wareing - Random Cards");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Picks four random cards that are not in the hand currently displayed
     * and loads their images into the four image views.
     */
    private void dealHand() {
        // Build the list of candidate card numbers, skipping the cards on screen.
        final List<Integer> previous = currentHand;
        List<Integer> deck = IntStream.rangeClosed(1, DECK_SIZE)
                .boxed()
                .filter(n -> !previous.contains(n))
                .collect(Collectors.toList());

        // Shuffle and take the first four.
        Collections.shuffle(deck);
        currentHand = new ArrayList<>(deck.subList(0, HAND_SIZE));

        // Load each chosen card into its slot (lambda inside forEach).
        IntStream.range(0, HAND_SIZE).forEach(i ->
                cardViews.get(i).setImage(loadCard(currentHand.get(i))));
    }

    /**
     * Loads the image for one card from the cards subdirectory. If the file
     * is missing or cannot be read, an error is written to the console and
     * null is returned so that slot is left blank instead of crashing.
     *
     * @param cardNumber card number from 1 to 52
     * @return the card image, or null if it could not be loaded
     */
    private Image loadCard(int cardNumber) {
        File file = new File(CARD_DIR, cardNumber + ".png");

        if (!file.isFile()) {
            System.err.println("Error: Card image not found: " + file.getAbsolutePath());
            return null;
        }

        try {
            Image image = new Image(file.toURI().toString());
            if (image.isError()) {
                String reason = (image.getException() != null)
                        ? image.getException().getMessage()
                        : "unknown reason";
                System.err.println("Error: Could not load card image "
                        + file.getName() + " (" + reason + ").");
                return null;
            }
            return image;
        } catch (IllegalArgumentException ex) {
            System.err.println("Error: Invalid path for card image "
                    + file.getName() + " (" + ex.getMessage() + ").");
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
