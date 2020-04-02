/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travail_pratique_3;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author carm
 */
public class Travail_Pratique_3 extends Application {
    
    private static final int W = 6;
    private static final int H = 6;
    private static final int SIZE = 100;

    private Color[] colors = new Color[] {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };

    private Boule selected = null;
    private List<Boule> Tab_Boules;

    private IntegerProperty score = new SimpleIntegerProperty();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W * SIZE + 150, H * SIZE);

        Tab_Boules = IntStream.range(0, W * H)
                .mapToObj(i -> new Point2D(i % W, i / W))
                .map(Boule::new)
                .collect(Collectors.toList());

        root.getChildren().addAll(Tab_Boules);

        Text textScore = new Text();
        textScore.setTranslateX(W * SIZE);
        textScore.setTranslateY(100);
        textScore.setFont(Font.font(68));
        textScore.textProperty().bind(score.asString("Score: [%d]"));

        root.getChildren().add(textScore);
        return root;
    }

    private void checkState() {
        Map<Integer, List<Boule>> rows = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getRow));
        Map<Integer, List<Boule>> columns = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getColumn));

        rows.values().forEach(this::checkCombo);
        columns.values().forEach(this::checkCombo);
    }

    private void checkCombo(List<Boule> Tab_BoulesLine) {
        Boule jewel = Tab_BoulesLine.get(0);
        long count = Tab_BoulesLine.stream().filter(j -> j.getColor() != jewel.getColor()).count();
        if (count == 0) {
            score.set(score.get() + 1000);
            Tab_BoulesLine.forEach(Boule::randomize);
        }
    }

    private void swap(Boule a, Boule b) {
        Paint color = a.getColor();
        a.setColor(b.getColor());
        b.setColor(color);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private class Boule extends Parent {
        private Circle circle = new Circle(SIZE / 2);

        public Boule(Point2D point) {
            circle.setCenterX(SIZE / 2);
            circle.setCenterY(SIZE / 2);
            circle.setFill(colors[new Random().nextInt(colors.length)]);

            setTranslateX(point.getX() * SIZE);
            setTranslateY(point.getY() * SIZE);
            getChildren().add(circle);

            setOnMouseClicked(event -> {
                if (selected == null) {
                    selected = this;
                }
                else {
                    swap(selected, this);
                    checkState();
                    selected = null;
                }
            });
        }

        public void randomize() {
            circle.setFill(colors[new Random().nextInt(colors.length)]);
        }

        public int getColumn() {
            return (int)getTranslateX() / SIZE;
        }

        public int getRow() {
            return (int)getTranslateY() / SIZE;
        }

        public void setColor(Paint color) {
            circle.setFill(color);
        }

        public Paint getColor() {
            return circle.getFill();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
