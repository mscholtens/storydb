package model;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Marijn on 23.12.17.
 */
public class StoryModel extends Observable {

    private ArrayList<Appearance> appearances;
    private ArrayList<Person> persons;
    private ArrayList<Book> books;
    private ArrayList<Era> eras;

    public StoryModel() {
        persons = new ArrayList<Person>();
        eras = new ArrayList<Era>();
        books = new ArrayList<Book>();
        appearances = new ArrayList<Appearance>();
        addEra("Unassigned", 0, 0);
        setChanged();
        notifyObservers();
    }

    public void clearModel() {
        appearances.clear();
        books.clear();
        eras.clear();
        persons.clear();
        setChanged();
        notifyObservers();
    }

    public void saveModel() {
        JTextField nameField = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter a name without extension:"));
        panel.add(nameField);
        String fileName = nameField.getText() + ".txt";
        BufferedWriter writer = null;
        JFileChooser saveFile = new JFileChooser();
        saveFile.setSelectedFile(new File(fileName));
        int sf = saveFile.showSaveDialog(saveFile);

        if(sf == JFileChooser.APPROVE_OPTION) {
            try {
                writer = new BufferedWriter(new FileWriter(saveFile.getSelectedFile()));
                writer.write(persons.size() + "," + eras.size() + "," + books.size() + "," + appearances.size());
                writer.write("\n");
                for(Person p : persons) {
                    writer.write(p.getName() + "," + p.getNickNamesMerged() + "," + p.getAltNamesMerged() + "," + p.isSex() + "," + p.getTitle() + "," + p.isVip() + "," + p.getPlaceOfBirth() + "," + p.getResidence() + "," + p.getDescription());
                    writer.write("\n");
                }
                for(Era e : eras) {
                    writer.write(e.getName() + "," + Integer.toString(e.getStartYear()) + "," + Integer.toString(e.getEndYear()));
                    writer.write("\n");
                }
                for(Book b : books) {
                    writer.write(b.getName() + "," + b.getEra().getName());
                    writer.write("\n");
                }
                for(Appearance a : appearances) {
                    writer.write(a.getPerson().getName() + "," + a.getBook().getName() + "," + a.getDescription());
                    writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Saving story model failed: Exception 1 (Bad file).");
            } finally {
                try {
                    if(writer != null) {
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Saving story model successful.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Saving story model failed: Exception 2 (Failed close).");
                }
            }
        } else if(sf == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "Saving story model failed: cancelled.");
        }
    }

    public void loadModel() {
        BufferedReader reader = null;
        JFileChooser loadFile = new JFileChooser();
        int lf = loadFile.showOpenDialog(loadFile);
        boolean loadSuccess = false;

        if(lf == JFileChooser.APPROVE_OPTION) {
            try {
                reader = new BufferedReader(new FileReader(loadFile.getSelectedFile()));
                String fileName = loadFile.getSelectedFile().toString();
                // check for correctness
                if(!fileName.endsWith(".txt")) {
                    JOptionPane.showMessageDialog(null, "Loading story model failed: Input file must be of type .txt");
                } else if(!isCorrectLoadInput(reader)) {
                    JOptionPane.showMessageDialog(null, "Loading story model failed: Error in the input.");
                } else {
                    reader = new BufferedReader(new FileReader(loadFile.getSelectedFile()));
                    loadAndParse(reader);
                }

                // exception handling and closing
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Loading story model failed: Exception 1 (Bad file).");
            } finally {
                try {
                    if(reader != null) {
                        reader.close();
                        if(loadSuccess) {
                            JOptionPane.showMessageDialog(null, "Loading story model successful.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Loading story model failed: Exception 2 (Failed close).");
                }
            }
        } else if(lf == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "Loading story model failed: cancelled.");
        }
        setChanged();
        notifyObservers();
    }

    public void addAppearance(String person, String book, String description) {
        Appearance appearance = new Appearance(findPerson(person), findBook(book), description);
        appearances.add(appearance);
        setChanged();
        notifyObservers();
    }

    public synchronized void deleteAppearance(String person, String book) {
        Appearance a = findAppearance(person, book);
        appearances.remove(a);
        setChanged();
        notifyObservers();
    }

    public Appearance findAppearance(String person, String book) {
        for(Appearance a : appearances) {
            if(a.getPerson().getName().equals(person) && a.getBook().getName().equals(book)) {
                return a;
            }
        }
        return null;
    }

    public void addPerson(String name, String nicknames, String altnames, String sex, String title, String vip,
                          String placeOfBirth, String residence, String description) {
        Person person = new Person(name, nicknames, altnames, sex, title, vip, placeOfBirth, residence, description);
        persons.add(person);
        sortPersons();
        setChanged();
        notifyObservers();
    }

    public void editPerson(String name, String nicknames, String altnames, String sex, String title, String vip,
                           String placeOfBirth, String residence, String description, Person p) {
        p.setName(name);
        p.setNicknames(nicknames.split(","));
        p.setAltnames(altnames.split(","));
        if(sex == "Male") {
            p.setSex(true);
        } else {
            p.setSex(false);
        }
        p.setTitle(title);
        if(vip == "Yes") {
            p.setVip(true);
        } else {
            p.setVip(false);
        }
        p.setPlaceOfBirth(placeOfBirth);
        p.setResidence(residence);
        p.setDescription(description);
        sortPersons();
        setChanged();
        notifyObservers();
    }

    public void searchPerson(String name) {
        for (Person p : persons) {
            if (p.getName().equals(name)) {
                p.setHighlighted(true);
                setChanged();
                notifyObservers();
                return;
            }
        }
        showMessageDialog(null, "Warning: Person could not be searched.");
    }

    public Person findPerson(String name) {
        for (Person p : persons) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        showMessageDialog(null, "Warning: Person could not be found.");
        return null;
    }

    public void resetSearchPerson() {
        for (Person p : persons) {
            if(p.isHighlighted()) {
                p.setHighlighted(false);
            }
        }
        setChanged();
        notifyObservers();
    }

    public synchronized void deletePerson(String name) {
        for (Person p : persons) {
            if(p.getName().equals(name)) {
                persons.remove(p);
            }
        }
        sortPersons();
        setChanged();
        notifyObservers();
    }

    public void addBook(String name, String eraName) {
        Book book = new Book(name, findEra(eraName));
        books.add(book);
        setChanged();
        notifyObservers();
    }

    public void editBook(String name, String e, Book b) {
        b.setName(name);
        b.setEra(findEra(e));
        setChanged();
        notifyObservers();
    }

    public synchronized void deleteBook(String name) {
        for (Book b : books) {
            if(b.getName().equals(name)) {
                books.remove(b);
            }
        }
        setChanged();
        notifyObservers();
    }

    public Book findBook(String name) {
        for (Book b : books) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        showMessageDialog(null, "Warning: Book could not be found.");
        return null;
    }

    public void addEra(String name, int startYear, int endYear) {
        Era era = new Era(name, startYear, endYear);
        eras.add(era);
        setChanged();
        notifyObservers();
    }

    public void editEra(String name, int startYear, int endYear, Era e) {
        if(!name.equals("Unassigned")) {
            e.setName(name);
            e.setStartYear(startYear);
            e.setEndYear(endYear);
        }
        setChanged();
        notifyObservers();
    }

    public synchronized void deleteEra(String name) {
        for (Era e : eras) {
            if(e.getName().equals(name)) {
                if(!name.equals("Unassigned")) {
                    for(Book b : books) {
                        if(b.getEra().getName().equals(name)) {
                            b.setEra(findEra("Unassigned"));
                        }
                    }
                    eras.remove(e);
                    break;
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    public Era findEra(String name) {
        for (Era e : eras) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        showMessageDialog(null, "Warning: Era could not be found.");
        return null;
    }

    public ArrayList<Appearance> getAppearances() {
        return appearances;
    }

    public void setAppearances(ArrayList<Appearance> appearances) {
        this.appearances = appearances;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Era> getEras() {
        return eras;
    }

    public void setEras(ArrayList<Era> eras) {
        this.eras = eras;
    }

    public void updateModel() {
        setChanged();
        notifyObservers();
    }

    private void sortPersons() {
        Collections.sort(persons, (person1, person2) -> person1.getName().compareTo(person2.getName()));
    }

    private boolean isCorrectLoadInput(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        String[] overheadArray = line.split(",", -1);
        if(overheadArray.length != 4) {
            return false;
        }
        int nPersons = Integer.parseInt(overheadArray[0]);
        int nEras = Integer.parseInt(overheadArray[1]);
        int nBooks = Integer.parseInt(overheadArray[2]);
        int nAppearances = Integer.parseInt(overheadArray[3]);
        int i;
        String[] personArray, eraArray, bookArray, appearanceArray;

        for(i = 0; i < nPersons; ++i) {
            line = reader.readLine();
            if(line == null) {
                return false;
            }
            personArray = line.split(",", -1);
            if(personArray.length != 9) {
                return false;
            }
        }
        for(i = 0; i < nEras; ++i) {
            line = reader.readLine();
            if(line == null) {
                return false;
            }
            eraArray = line.split(",", -1);
            if(eraArray.length != 3) {
                return false;
            }
        }
        for(i = 0; i < nBooks; ++i) {
            line = reader.readLine();
            if(line == null) {
                return false;
            }
            bookArray = line.split(",", -1);
            if(bookArray.length != 2) {
                return false;
            }
        }
        for(i = 0; i < nAppearances; ++i) {
            line = reader.readLine();
            if(line == null) {
                return false;
            }
            appearanceArray = line.split(",", -1);
            if(appearanceArray.length != 3) {
                return false;
            }
        }
        return true;
    }

    private void loadAndParse(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        String[] overheadArray = line.split(",", -1);
        int nPersons = Integer.parseInt(overheadArray[0]);
        int nEras = Integer.parseInt(overheadArray[1]);
        int nBooks = Integer.parseInt(overheadArray[2]);
        int nAppearances = Integer.parseInt(overheadArray[3]);
        int i;
        String[] personArray, eraArray, bookArray, appearanceArray;

        for(i = 0; i < nPersons; ++i) {
            line = reader.readLine();
            personArray = line.split(",", -1);
            addPerson(personArray[0], personArray[1], personArray[2], personArray[3], personArray[4], personArray[5], personArray[6], personArray[7], personArray[8]);
        }
        for(i = 0; i < nEras; ++i) {
            line = reader.readLine();
            eraArray = line.split(",", -1);
            addEra(eraArray[0], Integer.parseInt(eraArray[1]), Integer.parseInt(eraArray[2]));
        }
        for(i = 0; i < nBooks; ++i) {
            line = reader.readLine();
            bookArray = line.split(",", -1);
            addBook(bookArray[0], bookArray[1]);
        }
        for(i = 0; i < nAppearances; ++i) {
            line = reader.readLine();
            appearanceArray = line.split(",", -1);
            addAppearance(appearanceArray[0], appearanceArray[1], appearanceArray[2]);
        }
    }
}
