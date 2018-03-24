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
public class PersonController extends MouseAdapter implements StoryDBConstants {

    StoryModel model;
    int totalColWidth;
    private ArrayList<Appearance> appearances;
    private ArrayList<Person> persons;

    public PersonController(StoryModel model) {
        this.model = model;
        totalColWidth = 0;
        this.appearances = model.getAppearances();
        this.persons = model.getPersons();
    }

    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();

        if(SwingUtilities.isLeftMouseButton(e) && !e.isConsumed()) {
            e.consume();
            // conditions and effects, use model.getXXX() and make changes accordingly
            for(Person person : persons) {
                if(isSelected(person, p)) {
                    if(!person.isExpanded()) {
                        person.expand();
                        person.setHighlighted(true);
                    } else {
                        person.collapse();
                        person.setHighlighted(false);
                    }
                    model.updateModel();
                }
            }
        }

        Component source = (Component)e.getSource();
        source.repaint();
    }

    private boolean isSelected(Person person, Point p) {
        double pointX = p.getX();
        double pointY = p.getY();
        double leftX = COLUMNBORDERINIT;
        double topY = ROWBORDERINIT + computeRowAddition(person);
        double rightX = leftX + totalColWidth;
        double bottomY = topY + ROWBORDERDIFF;

        if((pointX > leftX && pointX < rightX) && (pointY > topY && pointY < bottomY)) {
            return true;
        }
        return false;
    }

    private int computeRowAddition(Person selected) {
        int addition = 0;
        for(Person person : persons) {
            addition += ROWBORDERDIFF;
            if(person.getName().equals(selected.getName())) {
                break;
            }
            if(person.isExpanded()) {
                for(Appearance a : appearances) {
                    if(a.getPerson().equals(person)) {
                        addition += ROWBORDERDIFF;
                    }
                }
            }
        }
        addition -= ROWBORDERDIFF;
        return addition;
    }

    public void setTotalColWidth(int totalColWidth) {
        this.totalColWidth = totalColWidth;
    }
}