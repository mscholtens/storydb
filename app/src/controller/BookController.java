package controller;

import core.StoryDBConstants;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Marijn on 23.12.17.
 */
public class BookController extends MouseAdapter implements StoryDBConstants {

    StoryModel model;
    int totalColWidth;
    private ArrayList<Book> books;
    private ArrayList<Era> eras;
    private ArrayList<Appearance> appearances;

    public BookController(StoryModel model) {
        this.model = model;
        this.totalColWidth = 0;
        this.books = model.getBooks();
        this.eras = model.getEras();
        this.appearances = model.getAppearances();
    }

    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();

        if(SwingUtilities.isLeftMouseButton(e) && !e.isConsumed()) {
            e.consume();
            // conditions and effects, use model.getXXX() and make changes accordingly
            for(Book b : books) {
                if(isSelected(b, p)) {
                    if(!b.isExpanded()) {
                        b.expand();
                        b.setHighlighted(true);
                    } else {
                        b.collapse();
                        b.setHighlighted(false);
                    }
                    model.updateModel();
                    break;
                }
            }
        }

        Component source = (Component)e.getSource();
        source.repaint();
    }

    private boolean isSelected(Book b, Point p) {
        double pointX = p.getX();
        double pointY = p.getY();
        double leftX = COLUMNBORDERINIT;
        double topY = ROWBORDERINIT + computeRowAddition(b);
        double rightX = leftX + totalColWidth;
        double bottomY = topY + ROWBORDERDIFF;

        if((pointX > leftX && pointX < rightX) && (pointY > topY && pointY < bottomY)) {
            return true;
        }
        return false;
    }

    private int computeRowAddition(Book selected) {
        int addition = 0;
        eraloop:
        for(Era era : eras) {
            addition += 2*ROWBORDERDIFF;
            for (Book book : books) {
                if(book.getEra().equals(era)) {
                    if (book.getName().equals(selected.getName())) {
                        break eraloop;
                    }
                    addition += ROWBORDERDIFF;
                    if (book.isExpanded()) {
                        for (Appearance a : appearances) {
                            if (a.getBook().equals(book)) {
                                addition += ROWBORDERDIFF;
                            }
                        }
                    }
                }
            }
            addition += ROWBORDERDIFF;
        }
        addition -= ROWBORDERDIFF;
        return addition;

        /*int addition = 0;
        for(Book b : books) {
            for(Era e : eras) {
                addition += 2*ROWBORDERDIFF;
                if(b.getEra().equals(e)) {
                    if (b.getName().equals(selected.getName())) {
                        break;
                    }
                    addition += ROWBORDERDIFF;
                    if (b.isExpanded()) {
                        for (Appearance a : appearances) {
                            if (a.getBook().equals(b)) {
                                addition += ROWBORDERDIFF;
                            }
                        }
                    }
                }
                addition += ROWBORDERDIFF;
            }
        }
        return addition;*/
    }

    public void setTotalColWidth(int totalColWidth) {
        this.totalColWidth = totalColWidth;
    }

}