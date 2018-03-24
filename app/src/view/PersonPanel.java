package view;

import controller.PersonController;
import core.StoryDBConstants;
import model.Appearance;
import model.Book;
import model.Person;
import model.StoryModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Marijn on 23.12.17.
 */
public class PersonPanel extends JPanel implements Observer, StoryDBConstants {

    private StoryModel model = null;
    private PersonController controller;
    private ArrayList<Person> persons;
    private ArrayList<Appearance> appearances;

    private JPanel topPanel;
    private JPanel bottomPanel;

    public PersonPanel(StoryModel model) {
        this.model = model;
        setModel(model);
        controller = new PersonController(model);
        setController(controller);
        initComponents();
    }

    public void setController(PersonController mousec) {
        this.addMouseListener(mousec);
        this.addMouseMotionListener(mousec);
    }

    public void setModel(StoryModel model) {
        if(this.model != null) {
            this.model.deleteObserver(this);
        }
        this.model = model;
        this.model.addObserver(this);
    }

    public StoryModel getModel() {
        return model;
    }

    private void initComponents() {
        topPanel = new JPanel();
        bottomPanel = new JPanel() {
            int x = COLUMNBORDERINIT;
            int y = ROWBORDERINIT;
            int totalColWidth = 0;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                persons = model.getPersons();
                appearances = model.getAppearances();
                if(!persons.isEmpty()) {
                    Collections.sort(persons, (person1, person2) -> person1.getName().compareTo(person2.getName()));
                    g.setFont(new Font(PERSONFONTTYPE, Font.BOLD, PERSONFONTSIZE));
                    int colWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("#");
                    int colWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Name");
                    int colWidth3 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Nicknames");
                    int colWidth4 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Altnames");
                    int colWidth5 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Sex");
                    int colWidth6 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Title");
                    int colWidth7 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Vip");
                    int colWidth8 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Place of Birth");
                    int colWidth9 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Residence");
                    int colWidth10 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Description");
                    int appWidth1 = COLUMNWIDTHADD;
                    int appWidth2 = COLUMNWIDTHADD;
                    int appWidth3 = COLUMNWIDTHADD;
                    int i = 1;
                    for (Person p : persons) {
                        if (g.getFontMetrics().stringWidth(p.getName()) > colWidth2 - COLUMNWIDTHADD) {
                            colWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getName()) + g.getFontMetrics().stringWidth("Name");
                        }
                        if (g.getFontMetrics().stringWidth(p.getNickNamesMerged()) > colWidth3 - COLUMNWIDTHADD) {
                            colWidth3 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getNickNamesMerged()) + g.getFontMetrics().stringWidth("Nicknames");
                        }
                        if (g.getFontMetrics().stringWidth(p.getAltNamesMerged()) > colWidth4 - COLUMNWIDTHADD) {
                            colWidth4 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getAltNamesMerged()) + g.getFontMetrics().stringWidth("Altnames");
                        }
                        if (g.getFontMetrics().stringWidth(p.getTitle()) > colWidth6 - COLUMNWIDTHADD) {
                            colWidth6 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getTitle()) + g.getFontMetrics().stringWidth("Title");
                        }
                        if (g.getFontMetrics().stringWidth(p.getTitle()) > colWidth8 - COLUMNWIDTHADD) {
                            colWidth8 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getTitle()) + g.getFontMetrics().stringWidth("Place of Birth");
                        }
                        if (g.getFontMetrics().stringWidth(p.getTitle()) > colWidth9 - COLUMNWIDTHADD) {
                            colWidth9 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getTitle()) + g.getFontMetrics().stringWidth("Residence");
                        }
                        if (g.getFontMetrics().stringWidth(p.getDescription()) > colWidth10 - COLUMNWIDTHADD) {
                            colWidth10 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(p.getDescription()) + g.getFontMetrics().stringWidth("Description");
                        }
                    }
                    for(Appearance a : appearances) {
                        if(g.getFontMetrics().stringWidth(a.getBook().getName()) > appWidth1 - COLUMNWIDTHADD) {
                            appWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(a.getBook().getName());
                        }
                        if(g.getFontMetrics().stringWidth(a.getBook().getEra().getName()) > appWidth2 - COLUMNWIDTHADD) {
                            appWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(a.getBook().getEra().getName());
                        }
                        if(g.getFontMetrics().stringWidth(a.getDescription()) > appWidth3 - COLUMNWIDTHADD) {
                            appWidth3 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(a.getDescription());
                        }
                    }
                    colWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("" + persons.size()) + g.getFontMetrics().stringWidth("#");
                    colWidth5 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Female") + g.getFontMetrics().stringWidth("Sex"); // Male or Female
                    colWidth7 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Yes") + g.getFontMetrics().stringWidth("Vip"); // Yes or No
                    totalColWidth = colWidth1 + colWidth2 + colWidth3 + colWidth4 + colWidth5 + colWidth6 + colWidth7 + colWidth8 + colWidth9 + colWidth10;
                    int totalAppWidth = colWidth1 + appWidth1 + appWidth2 + appWidth3;
                    totalColWidth = totalAppWidth > totalColWidth ? totalAppWidth : totalColWidth;
                    controller.setTotalColWidth(totalColWidth);

                    // build table
                    y = ROWBORDERINIT;
                    g.drawString("#", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth1;
                    g.drawString("Name", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth2;
                    g.drawString("Nicknames", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth3;
                    g.drawString("Altnames", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth4;
                    g.drawString("Sex", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth5;
                    g.drawString("Title", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth6;
                    g.drawString("Vip", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth7;
                    g.drawString("Place of Birth", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth8;
                    g.drawString("Residence", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth9;
                    g.drawString("Description", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth10;
                    g.setFont(new Font(PERSONFONTTYPE, Font.PLAIN, PERSONFONTSIZE));
                    x = COLUMNBORDERINIT;
                    y += ROWBORDERDIFF;
                    for (Person p : persons) {
                        if (p.isHighlighted()) {
                            // selected, highlight
                            g.setColor(Color.PINK);
                            g.fillRect(x, y - ROWBORDERDIFF, totalColWidth, ROWBORDERDIFF);
                        } else if (i % 2 == 1) {
                            // odd, draw rectangle with color 1
                            g.setColor(Color.WHITE);
                            g.fillRect(x, y - ROWBORDERDIFF, totalColWidth, ROWBORDERDIFF);
                        } else {
                            // even, draw rectangle with color 2
                            g.setColor(Color.LIGHT_GRAY);
                            g.fillRect(x, y - ROWBORDERDIFF, totalColWidth, ROWBORDERDIFF);
                        }
                        g.setColor(Color.BLACK);
                        g.drawLine(x, y - ROWBORDERDIFF, x + totalColWidth, y - ROWBORDERDIFF);
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString("" + i, x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth1;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getName(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth2;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getNickNamesMerged(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth3;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getAltNamesMerged(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth4;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        if (p.isSex()) {
                            g.drawString("Male", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        } else {
                            g.drawString("Female", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        }
                        x += colWidth5;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getTitle(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth6;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        if (p.isVip()) {
                            g.drawString("Yes", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        } else {
                            g.drawString("No", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        }
                        x += colWidth7;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getPlaceOfBirth(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth8;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getResidence(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth9;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        g.drawString(p.getDescription(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                        x += colWidth10;
                        g.drawLine(x, y - ROWBORDERDIFF, x, y);
                        x = COLUMNBORDERINIT;
                        y += ROWBORDERDIFF;
                        g.drawLine(x, y - ROWBORDERDIFF, x + totalColWidth, y - ROWBORDERDIFF);
                        ++i;
                        if(p.isExpanded()) {
                            x += colWidth1;
                            for(Appearance a : model.getAppearances()) {
                                if(a.getPerson().equals(p)) {
                                    g.drawString(a.getBook().getName(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                                    x += appWidth1;
                                    g.drawString(a.getBook().getEra().getName(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                                    x += appWidth2;
                                    g.drawString(a.getDescription(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                                    x += appWidth3;
                                    y += ROWBORDERDIFF;
                                }
                                x = COLUMNBORDERINIT + colWidth1;
                            }
                            x = COLUMNBORDERINIT;
                        }
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                int width = INITFRAMEWIDTH + (totalColWidth - INITFRAMEWIDTH + 2*COLUMNBORDERINIT > 0 ? totalColWidth - INITFRAMEWIDTH + 2*COLUMNBORDERINIT : 0);
                int height = INITFRAMEHEIGHT - TOPPANELOFFSET - ROWBORDERINIT;
                height += (y - INITFRAMEHEIGHT + 2*ROWBORDERINIT + ROWBORDERDIFF > 0 ? y - INITFRAMEHEIGHT + 2*ROWBORDERINIT + ROWBORDERDIFF: 0);
                return new Dimension(width, height);
            }
        };
        bottomPanel.addMouseListener(controller);
        bottomPanel.addMouseMotionListener(controller);

        String name;
        String nicknames;
        String altnames;
        String sex;
        String title;
        String vip;
        String placeOfBirth;
        String residence;
        String description;

        // add person button
        JButton addPersonButton = new JButton("Add Character");

        addPersonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTextField nameField = new JTextField("");
                JTextField nicknamesField = new JTextField("");
                JTextField altnamesField = new JTextField("");
                JTextField titleField = new JTextField("");
                JTextField placeOfBirthField = new JTextField("");
                JTextField residenceField = new JTextField("");
                JTextField descriptionField = new JTextField("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Nicknames (comma-separated):"));
                panel.add(nicknamesField);
                panel.add(new JLabel("Altnames (comma-separated):"));
                panel.add(altnamesField);
                panel.add(new JLabel("Sex:"));
                String[] sexChoice = {"Male", "Female"};
                JComboBox<String> sexField = new JComboBox<>(sexChoice);
                panel.add(sexField);
                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Vip:"));
                String[] vipChoice = {"No", "Yes"};
                JComboBox<String> vipField = new JComboBox<>(vipChoice);
                panel.add(vipField);
                panel.add(new JLabel("Place of Birth:"));
                panel.add(placeOfBirthField);
                panel.add(new JLabel("Residence:"));
                panel.add(residenceField);
                panel.add(new JLabel("Description:"));
                panel.add(descriptionField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Add Character",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    model.addPerson(nameField.getText(), nicknamesField.getText(), altnamesField.getText(), (String)sexField.getSelectedItem(),
                            titleField.getText(), (String)vipField.getSelectedItem(), placeOfBirthField.getText(),
                            residenceField.getText(), descriptionField.getText());
                }
            }
        });
        topPanel.add(addPersonButton);

        // edit person button
        JButton editPersonButton = new JButton("Edit Character");

        editPersonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the character to edit. Leave empty to cancel.");
                if(name != null && name != "") {
                    Person p = model.findPerson(name);
                    if(p != null) {
                        JTextField nameField = new JTextField(p.getName());
                        JTextField nicknamesField = new JTextField(p.getNickNamesMerged());
                        JTextField altnamesField = new JTextField(p.getAltNamesMerged());
                        JTextField titleField = new JTextField(p.getTitle());
                        JTextField placeOfBirthField = new JTextField(p.getPlaceOfBirth());
                        JTextField residenceField = new JTextField(p.getResidence());
                        JTextField descriptionField = new JTextField(p.getDescription());
                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(new JLabel("Name:"));
                        panel.add(nameField);
                        panel.add(new JLabel("Nicknames (comma-separated):"));
                        panel.add(nicknamesField);
                        panel.add(new JLabel("Altnames (comma-separated):"));
                        panel.add(altnamesField);
                        panel.add(new JLabel("Sex:"));
                        String[] sexChoice = {"Male", "Female"};
                        JComboBox<String> sexField = new JComboBox<>(sexChoice);
                        if (p.isSex()) {
                            sexField.setSelectedItem("Male");
                        } else {
                            sexField.setSelectedItem("Female");
                        }
                        panel.add(sexField);
                        panel.add(new JLabel("Title:"));
                        panel.add(titleField);
                        panel.add(new JLabel("Vip:"));
                        String[] vipChoice = {"No", "Yes"};
                        JComboBox<String> vipField = new JComboBox<>(vipChoice);
                        if (p.isVip()) {
                            vipField.setSelectedItem("Yes");
                        } else {
                            vipField.setSelectedItem("No");
                        }
                        panel.add(vipField);
                        panel.add(new JLabel("Place of Birth:"));
                        panel.add(placeOfBirthField);
                        panel.add(new JLabel("Residence:"));
                        panel.add(residenceField);
                        panel.add(new JLabel("Description:"));
                        panel.add(descriptionField);
                        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Character",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            model.editPerson(nameField.getText(), nicknamesField.getText(), altnamesField.getText(), (String) sexField.getSelectedItem(),
                                    titleField.getText(), (String) vipField.getSelectedItem(), placeOfBirthField.getText(),
                                    residenceField.getText(), descriptionField.getText(), p);
                        }
                    }
                }
            }
        });
        topPanel.add(editPersonButton);

        // delete person button
        JButton deletePersonButton = new JButton("Delete Character");

        deletePersonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the character to delete. Leave empty to cancel.");
                if(name != null && name != "") {
                    model.deletePerson(name);
                }
            }
        });
        topPanel.add(deletePersonButton);

        // add appearance
        JButton addAppearanceButton = new JButton("Add Appearance");
        addAppearanceButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String[] personChoice = insertPersons();
                JComboBox<String> personField = new JComboBox<>(personChoice);
                String[] bookChoice = insertBooks();
                JComboBox<String> bookField = new JComboBox<>(bookChoice);
                JTextField descriptionField = new JTextField("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Person"));
                panel.add(personField);
                panel.add(new JLabel("Book"));
                panel.add(bookField);
                panel.add(new JLabel("Description"));
                panel.add(descriptionField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Add Book",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    model.addAppearance((String)personField.getSelectedItem(), (String)bookField.getSelectedItem(), descriptionField.getText());
                }
            }
        });
        topPanel.add(addAppearanceButton);

        // delete appearance button
        JButton deleteAppearanceButton = new JButton("Delete Appearance");
        deleteAppearanceButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String[] personChoice = insertPersons();
                JComboBox<String> personField = new JComboBox<>(personChoice);
                String[] bookChoice = insertBooks();
                JComboBox<String> bookField = new JComboBox<>(bookChoice);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Person"));
                panel.add(personField);
                panel.add(new JLabel("Book"));
                panel.add(bookField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Delete Appearance",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    model.deleteAppearance((String)personField.getSelectedItem(), (String)bookField.getSelectedItem());
                }
            }
        });
        topPanel.add(deleteAppearanceButton);

        /*
        // search button
        JButton searchPersonButton = new JButton("Search Character");
        searchPersonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the character to search. Leave empty to cancel.");
                if(name != null && name != "") {
                    model.searchPerson(name);
                }
            }
        });
        topPanel.add(searchPersonButton);
        */

        /*
        // reset search button
        JButton resetSearchPersonButton = new JButton("Reset Search");
        resetSearchPersonButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                model.resetSearchPerson();
            }
        });
        topPanel.add(resetSearchPersonButton);
        */

        JScrollPane bottomScrollPane = new JScrollPane(bottomPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        topPanel.setPreferredSize(new Dimension(INITFRAMEWIDTH, ROWBORDERINIT));
        bottomScrollPane.setPreferredSize(new Dimension(INITFRAMEWIDTH, INITFRAMEHEIGHT - TOPPANELOFFSET - ROWBORDERINIT));
        bottomScrollPane.getVerticalScrollBar().setUnitIncrement(UNITINCREMENT);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomScrollPane);
        splitPane.setDividerLocation(TOPPANELOFFSET);
        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        this.add(splitPane);
    }

    public void update(Observable arg0, Object arg1) {
        repaint();
    }

    private String[] insertPersons() {
        int amount = model.getPersons().size();
        int i = 0;
        String[] personChoice = new String[amount];
        for(Person p : model.getPersons()) {
            personChoice[i] = p.getName();
            ++i;
        }
        return personChoice;
    }

    private String[] insertBooks() {
        int amount = model.getBooks().size();
        int i = 0;
        String[] bookChoice = new String[amount];
        for(Book b : model.getBooks()) {
            bookChoice[i] = b.getName();
            ++i;
        }
        return bookChoice;
    }
}
