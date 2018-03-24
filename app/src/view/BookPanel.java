package view;

import controller.BookController;
import core.StoryDBConstants;
import model.*;

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
public class BookPanel extends JPanel implements Observer, StoryDBConstants {

    private StoryModel model = null;
    private BookController controller;
    private ArrayList<Book> books;
    private ArrayList<Era> eras;
    private ArrayList<Appearance> appearances;

    private JPanel topPanel;
    private JPanel bottomPanel;

    public BookPanel(StoryModel model) {
        this.model = model;
        setModel(model);
        controller = new BookController(model);
        setController(controller);
        initComponents();
    }

    public void setController(BookController mousec) {
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

                eras = model.getEras();
                books = model.getBooks();
                appearances = model.getAppearances();
                Collections.sort(eras, (era1, era2) -> ((Integer)era1.getStartYear()).compareTo(era2.getStartYear()));

                int colWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("#");
                int colWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("Name");
                int appWidth1 = COLUMNWIDTHADD;
                int appWidth2 = COLUMNWIDTHADD;
                int i;
                for (Book b : books) {
                    if (g.getFontMetrics().stringWidth(b.getName()) > colWidth2 - COLUMNWIDTHADD) {
                        colWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(b.getName()) + g.getFontMetrics().stringWidth("Name");
                    }
                }
                for(Appearance a : appearances) {
                    if(g.getFontMetrics().stringWidth(a.getPerson().getName()) > appWidth1 - COLUMNWIDTHADD) {
                        appWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(a.getPerson().getName());
                    }
                    if(g.getFontMetrics().stringWidth(a.getDescription()) > appWidth2 - COLUMNWIDTHADD) {
                        appWidth2 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth(a.getDescription());
                    }
                }
                colWidth1 = COLUMNWIDTHADD + g.getFontMetrics().stringWidth("" + books.size()) + g.getFontMetrics().stringWidth("#");
                totalColWidth = colWidth1 + colWidth2;
                int totalAppWidth = colWidth1 + appWidth1 + appWidth2;
                totalColWidth = totalAppWidth > totalColWidth ? totalAppWidth : totalColWidth;
                controller.setTotalColWidth(totalColWidth);
                totalColWidth = colWidth1 + colWidth2;

                y = ROWBORDERINIT;
                for(Era e : eras) {
                    g.setFont(new Font(PERSONFONTTYPE, Font.BOLD, PERSONFONTSIZE));
                    g.drawString(e.getName() + " " + e.getStartYear() + "-" + e.getEndYear(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    y += ROWBORDERDIFF;
                    g.drawString("#", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth1;
                    g.drawString("Name", x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                    x += colWidth2;
                    g.setFont(new Font(PERSONFONTTYPE, Font.PLAIN, PERSONFONTSIZE));
                    x = COLUMNBORDERINIT;
                    y += ROWBORDERDIFF;
                    i = 1;
                    for (Book b : books) {
                        if(b.getEra().equals(e)) {
                            if (b.isHighlighted()) {
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
                            g.drawString(b.getName(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                            x += colWidth2;
                            g.drawLine(x, y - ROWBORDERDIFF, x, y);
                            x = COLUMNBORDERINIT;
                            y += ROWBORDERDIFF;
                            g.drawLine(x, y - ROWBORDERDIFF, x + totalColWidth, y - ROWBORDERDIFF);
                            ++i;
                            if(b.isExpanded()) {
                                x += colWidth1;
                                for(Appearance a : model.getAppearances()) {
                                    if(a.getBook().equals(b)) {
                                        g.drawString(a.getPerson().getName(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                                        x += appWidth1;
                                        g.drawString(a.getDescription(), x + COLUMNWIDTHHALFADD, y - ROWADDENDUM);
                                        x += appWidth2;
                                        y += ROWBORDERDIFF;
                                    }
                                    x = COLUMNBORDERINIT + colWidth1;
                                }
                                x = COLUMNBORDERINIT;
                            }
                        }
                    }
                    y += ROWBORDERDIFF;
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

        // vars

        // add book button
        JButton addBookButton = new JButton("Add Book");

        addBookButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTextField nameField = new JTextField("");
                String[] eraChoice = insertEras();
                JComboBox<String> eraField = new JComboBox<>(eraChoice);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Name"));
                panel.add(nameField);
                panel.add(new JLabel("Era"));
                panel.add(eraField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Add Book",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    model.addBook(nameField.getText(), (String)eraField.getSelectedItem());
                }
            }
        });
        topPanel.add(addBookButton);

        // edit book button
        JButton editBookButton = new JButton("Edit Book");

        editBookButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the character to edit. Leave empty to cancel.");
                if(name != null && name != "") {
                    Book b = model.findBook(name);
                    if(b != null) {
                        JTextField nameField = new JTextField();
                        String[] eraChoice = insertEras();
                        JComboBox<String> eraField = new JComboBox<>(eraChoice);
                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(new JLabel("Name"));
                        panel.add(nameField);
                        panel.add(new JLabel("Era"));
                        panel.add(eraField);
                        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Book",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            model.editBook(nameField.getText(), (String)eraField.getSelectedItem(), b);
                        }
                    }
                }
            }
        });
        topPanel.add(editBookButton);

        // delete book button
        JButton deleteBookButton = new JButton("Delete Book");

        deleteBookButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the book to delete. Leave empty to cancel.");
                if(name != null && name != "") {
                    model.deleteBook(name);
                }
            }
        });
        topPanel.add(deleteBookButton);

        // add era button
        JButton addEraButton = new JButton("Add Era");

        addEraButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTextField nameField = new JTextField("");
                JTextField startYearField = new JTextField("0");
                JTextField endYearField = new JTextField("0");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Era Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Start Year"));
                panel.add(startYearField);
                panel.add(new JLabel("End Year"));
                panel.add(endYearField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Add Era",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    model.addEra(nameField.getText(), Integer.parseInt(startYearField.getText()), Integer.parseInt(endYearField.getText()));
                }
            }
        });
        topPanel.add(addEraButton);

        // edit era button
        JButton editEraButton = new JButton("Edit Era");

        editEraButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the era to edit. Leave empty to cancel.");
                if(name != null && name != "") {
                    Era era = model.findEra(name);
                    if(era != null) {
                        JTextField nameField = new JTextField(era.getName());
                        JTextField startYearField = new JTextField(era.getStartYear());
                        JTextField endYearField = new JTextField(era.getEndYear());
                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(new JLabel("Era Name:"));
                        panel.add(nameField);
                        panel.add(new JLabel("Start Year"));
                        panel.add(startYearField);
                        panel.add(new JLabel("End Year"));
                        panel.add(endYearField);
                        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Era",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            model.editEra(nameField.getText(), Integer.parseInt(startYearField.getText()), Integer.parseInt(endYearField.getText()), era);
                        }
                    }
                }
            }
        });
        topPanel.add(editEraButton);

        // delete era button
        JButton deleteEraButton = new JButton("Delete Era");

        deleteEraButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String name = JOptionPane.showInputDialog(null, "Enter the name of the era to delete. Leave empty to cancel.");
                if(name != null && name != "") {
                    model.deleteEra(name);
                }
            }
        });
        topPanel.add(deleteEraButton);

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

    private String[] insertEras() {
        int amount = model.getEras().size();
        int i = 0;
        String[] eraChoice = new String[amount];
        for(Era e : model.getEras()) {
            eraChoice[i] = e.getName();
            ++i;
        }
        return eraChoice;
    }
}
