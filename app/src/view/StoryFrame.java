package view;

import core.StoryDBConstants;
import model.StoryModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Created by Marijn on 23.12.17.
 */
public class StoryFrame extends JFrame implements StoryDBConstants {

    public JPanel contentPane;
    public CardLayout cardLayout;

    private AbstractAction newAction;
    private AbstractAction saveAction;
    private AbstractAction loadAction;
    private AbstractAction quitAction;
    private AbstractAction bookAction;
    private AbstractAction personAction;
    private AbstractAction aboutAction;

    private StoryModel model;
    private BookPanel bookPanel;
    private PersonPanel personPanel;

    public StoryFrame(StoryModel model) {
        this.model = model;
        initGUI();
    }

    private void initGUI() {
        this.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(INITFRAMEWIDTH, INITFRAMEHEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("StoryDB - by Marijn Scholtens");

        contentPane = new JPanel();
        contentPane.setBackground(Color.decode("#FFFFFF"));
        cardLayout = new CardLayout();
        contentPane.setLayout(cardLayout);

        bookPanel = new BookPanel(model);
        personPanel = new PersonPanel(model);

        contentPane.add(bookPanel, "Book Panel");
        contentPane.add(personPanel, "Person Panel");

        initActions();

        // menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        fileMenu.add(this.newAction);
        fileMenu.add(this.saveAction);
        fileMenu.add(this.loadAction);
        fileMenu.add(this.quitAction);
        viewMenu.add(this.bookAction);
        viewMenu.add(this.personAction);
        helpMenu.add(this.aboutAction);

        this.setJMenuBar(menuBar);
        this.setContentPane(contentPane);
        this.setVisible(true);

        showPersonPanel();
    }

    /*
     * The following code describes the actions of the jmenubar content.
     */
    @SuppressWarnings("serial")
    private void initActions() {

        this.newAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clearModel();
            }
        };
        this.saveAction = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.saveModel();
            }
        };
        this.loadAction = new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.loadModel();
            }
        };

        this.quitAction = new AbstractAction("Quit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        this.bookAction = new AbstractAction("Show Books") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookPanel();
            }
        };

        this.personAction = new AbstractAction("Show Characters") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPersonPanel();
            }
        };

        this.aboutAction = new AbstractAction("About") {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, "StoryDB by Marijn Scholtens");
            }
        };

        // Short keys for the Action objects
        // this.quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    private void showBookPanel() {
        cardLayout.show(contentPane, "Book Panel");
    }

    private void showPersonPanel() {
        cardLayout.show(contentPane, "Person Panel");
    }

    private void setModel(StoryModel model) {
        this.model = model;
    }

    private StoryModel getModel() {
        return model;
    }
}
