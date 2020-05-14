package travail_pratique_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * *<p>
 * TP3: Question #2.
 * </p>
 * @author carm, vais
 * @version 2.0.0 2020/04/29
 */
public class Travail_Pratique_3 extends Application {
    
    /**
     * Nombre de cercle sur l'axe des x.
     */
    private static final int W = 6;
    /**
     * Nombre de cercle sur l'axe des y.
     */
    private static final int H = 6;
    /**
     * Valeur qui represente la grosseur des cercles.
     */
    private static final int SIZE = 100;

    /**
     * Tableau de couleur qui contient toutes les couleurs possibles des cercles.
     */
    private final Color[] colors = new Color[] {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };

    /**
     * Instance de la classe Boule, est egal a null.
     */
    private Boule selected = null;
    /**
     * List d'instance de la classe Boule.
     */
    private List<Boule> Tab_Boules;

    /**
     * Propriete qui entoure un integer qui represente le score du joueur.
     */
    private IntegerProperty score = new SimpleIntegerProperty();
    /**
     * Texte qui affiche le score du joueur.
     */
    private Text textScore = new Text();

    
    /**
     * @param primaryStage qui represente le stage principal de l'application.
     * Fonction qui met en place le Pane de la scene.
     * Le size de root est configurer en fonction de W, H, et SIZE.
     * Le range de la liste de Boule est egal a W x H, la liste est remplie, puis ajouter au Pane.
     * Le Text du score est configuer puis ajouter au Pane.
     * @return root, le Pane de la scene.
     */
    private Parent createContent(Stage primaryStage) {
        Pane root = new Pane();
        Pane grille = new Pane();
        root.setPrefSize(W * SIZE + 650, H * SIZE + 50);

        Tab_Boules = IntStream.range(0, W * H)
                .mapToObj(i -> new Point2D(i % W, i / W))
                .map(Boule::new)
                .collect(Collectors.toList());

        grille.getChildren().addAll(Tab_Boules);
        grille.setTranslateY(40);

        textScore.setTranslateX(W * SIZE);
        textScore.setTranslateY(100);
        textScore.setFont(Font.font(68));
        textScore.textProperty().bind(score.asString("Score: [%d]"));
        
        MenuBar bar = setMenuBar();
        bar.prefWidthProperty().bind(primaryStage.widthProperty());
        
        root.getChildren().addAll(textScore, bar, grille);
        return root;
    }
    
    /**
     * Fonction qui parametrise la barre de menu du programme avant de l'implementer.
     * @return temp qui represente la barre de menu du programme une fois configuree.
     */
    private MenuBar setMenuBar() {
        MenuBar temp = new MenuBar();
        
        Menu fileMenu = setFileMenu();
        Menu infoMenu = new Menu("?");
        
        MenuItem miShowVersion = new MenuItem("#Version...");
        miShowVersion.setOnAction(e -> showVersion());
        infoMenu.getItems().add(miShowVersion);
        
        temp.getMenus().addAll(fileMenu, infoMenu);
        return temp;
    }
    
    /**
     * Fonction qui parametrise le menu file de la barre de menu du programme.
     * La fonction ajoute de menus, soit pour commencer une nouvelle partie et quitter le programme.
     * @return temp qui represente le menu file de la barre de menu du programme.
     */
    private Menu setFileMenu() {
        Menu temp = new Menu("_File");
        
        MenuItem miNouvellePartie = new MenuItem("Nouvelle Partie");
        miNouvellePartie.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        miNouvellePartie.setOnAction(e -> {
            score = new SimpleIntegerProperty();
            textScore.textProperty().bind(score.asString("Score: [%d]"));
        });
        
        MenuItem miQuitter = new MenuItem("Quitter...");
        miQuitter.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+Q"));
        miQuitter.setOnAction(e -> {
            try {
                quitProgram();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Travail_Pratique_3.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        temp.getItems().addAll(miNouvellePartie, new SeparatorMenuItem(), miQuitter);
        
        return temp;
    }
    
    /**
     * Methode qui ouvre une fenetre qui affiche quelques informations sur le programmes.
     */
    private void showVersion() {
        Text nomVersion = new Text("TP3 Question #2 Version 2.0.0\n\n\nFait par: Martin Carignan et Simon-Olivier Vaillancourt.\n\n\n");
        Hyperlink lienGithub = new Hyperlink("Github de Simon-Olivier Vaillancourt");
        
        lienGithub.setOnAction(e -> getHostServices().showDocument("https://github.com/s0punk"));
        lienGithub.setFont(Font.font("SanSerif", 14));
        lienGithub.setBorder(Border.EMPTY);
        lienGithub.setTranslateX(25);
        lienGithub.setTranslateY(115);
        
        nomVersion.setFont(Font.font("SanSerif", 14));
        nomVersion.setTranslateX(25);
        nomVersion.setTranslateY(20);
        
        Pane root = new Pane();
        root.getChildren().addAll(nomVersion, lienGithub);
        
        Stage version = new Stage();
        version.setTitle("Version");
        version.setScene(new Scene(root, 1000, 200));
        version.setResizable(false);
        version.show();
    }
    
    /**
     * Methode qui demande a l'utilisateur une confirmation avant de quitter le programme pour s'assurer que son choix est intentionnelle.
     * Une nouvelle fenetre est creee afin d'isoler la confirmation du reste du programme.
     * @throws FileNotFoundException S'assure que l'icone utilisee est dans les dossiers du programmes.
     */
    private void quitProgram() throws FileNotFoundException {
        Button oui = new Button("Oui");
        Button non = new Button("Non");
        Button annuler = new Button("Annuler");
        Text avertissement = new Text();
        
        FileInputStream input = new FileInputStream("Icones/interrogation.jpg");
        Image interrogation_i = new Image(input);
        ImageView interrogation_iv = new ImageView(interrogation_i);
        interrogation_iv.setFitHeight(60);
        interrogation_iv.setFitWidth(60);
        Label interrogation = new Label("", interrogation_iv);
        
        avertissement.setText("ATTENTION ! Voulez-vous quitter le jeu ?\n\t Toute progrèssion sera perdue.");
        avertissement.setX(70);
        avertissement.setY(40);
        avertissement.setFont(Font.font("SanSerif", 15));
        
        interrogation.setTranslateX(5);
        interrogation.setTranslateY(20);
        
        oui = setButtonProperties(oui, 15, 110, 80, 5);
        non = setButtonProperties(non, 155, 110, 80, 5);
        annuler = setButtonProperties(annuler, 295, 110, 80, 5);
        
        Pane root = new Pane();
        ObservableList actor = root.getChildren();
        actor.addAll(non, oui, annuler, avertissement, interrogation);
        
        Stage confirmationButton = new Stage();
        confirmationButton.setTitle("Quitter");
        confirmationButton.setScene(new Scene(root, 400, 150));
        confirmationButton.setResizable(false);
        confirmationButton.show();
        
        oui.setOnAction(e -> Platform.exit());
        non.setOnAction(e -> confirmationButton.close());
        annuler.setOnAction(e -> confirmationButton.close());
    }
    
    /**
     * Parametrise les boutons
     * @param button bouton temporaire
     * @param posX position en X du bouton
     * @param posY postiion en Y du bouton
     * @param width largeur du bouton
     * @param height hauteur du bouton
     * @return retourne le bouton parametree
     */
    private Button setButtonProperties(Button button, int posX, int posY, int width, int height) {
        button.setTranslateX(posX);
        button.setTranslateY(posY);
        
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        
        return button;
    }

    /**
     * Methode qui analyse la grille de Boules afin de determiner si un combo a ete fait.
     * Chaque ligne et colonne est mit dans une liste de Boule, puis la methode checkCombo() est appelee avec la liste en parametre.
     */
    private void checkState() {
        Map<Integer, List<Boule>> rows = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getRow));
        Map<Integer, List<Boule>> columns = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getColumn));

        rows.values().forEach(this::checkCombo);
        columns.values().forEach(this::checkCombo);
    }

    /**
     * Methode qui verifie si un combo a ete effectue.
     * Si tout les Boules de la liste envoyee en parametre sont de la meme couleur, on additionne 1000 au score.
     * @param Tab_BoulesLine liste d'instance de la classe Boule.
     */
    private void checkCombo(List<Boule> Tab_BoulesLine) {
        Boule jewel = Tab_BoulesLine.get(0);
        long count = Tab_BoulesLine.stream().filter(j -> j.getColor() != jewel.getColor()).count();
        if (count == 0) {
            score.set(score.get() + 1000);
            Tab_BoulesLine.forEach(Boule::randomize);
            playComboSound();
        }
    }
    
    /**
     * Methode qui joue un AudioClip qui represente le son d'un combo.
     */
    private void playComboSound() {
        AudioClip comboSound = new AudioClip(new File("Sons/Houra_combo_sound.mp3").toURI().toString());
        comboSound.play();
    }

    /**
     * Methode qui interchange les couleurs de deux Boules envoyee en parametre.
     * @param a, une instance de la classe Boule. Sa couleur deviendra celle de la Boule b.
     * @param b , une instance de la classe Boule. Sa couleur deviendra celle de la Boule a.
     */
    private void swap(Boule a, Boule b) {
        Paint color = a.getColor();
        a.setColor(b.getColor());
        b.setColor(color);
    }

    @Override
    /**
     * Methode qui met en place le stage principale.
     */
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(primaryStage)));
        primaryStage.show();
    }

    /**
     * Classe Boule qui herite de Parent qui vient de javafx.scene.Parent.
     */
    private class Boule extends Parent {
        private Circle circle = new Circle(SIZE / 2);

        /**
         * Constructeur a 1 argument de la classe Boule.
         * @param point un Point2D qui represente une cooredonne x et y.
         */
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

        /**
         * Methode qui definie la couleur de la Boule de maniere aleatoire.
         * Un nombre est choisi aleatoirement entre 0 et la grosseur du tableau colors avec la fonction Random() de java.util.Random.
         */
        public void randomize() {
            circle.setFill(colors[new Random().nextInt(colors.length)]);
        }

        /**
         * Fonction qui prend la position du click de souris et renvoie la colonne de la grille de cerlces.
         * @return integer qui represente la colonne de la grille du cercle selectionner par l'utilisateur.
         */
        public int getColumn() {
            return (int)getTranslateX() / SIZE;
        }

        /**
         * Fonction qui prend la position du click de souris et renvoie la ligne de la grille de cerlces.
         * @return integer qui represente la ligne de la grille du cercle selectionner par l'utilisateur.
         */
        public int getRow() {
            return (int)getTranslateY() / SIZE;
        }

        /**
         * Methode qui definie la couleur d'une Boule avec la couleur qui est envoyee en parametre.
         * @param color couleur qui sera associee a la Boule.
         */
        public void setColor(Paint color) {
            circle.setFill(color);
        }

        /**
         * Fonction qui retourne la couleur de la Boule qui appelle la fonction.
         * @return Paint qui represente la couleur de la Boule.
         */
        public Paint getColor() {
            return circle.getFill();
        }
    }

    /**
     * Lance l'application.
     * @param args Argument en ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}