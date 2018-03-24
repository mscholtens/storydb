package core;

/**
 * Created by Marijn on 23.12.17.
 */
public interface StoryDBConstants {

    // program constants, when needed then add 'implements StoryDBConstants'
    public static final int INITFRAMEWIDTH = 860; // frame width
    public static final int INITFRAMEHEIGHT = 640; // frame height

    // table stuff
    public static final String PERSONFONTTYPE = "Calibri"; // font type
    public static final int PERSONFONTSIZE = 14; // font size
    public static final int TOPPANELOFFSET = 40; // height of the top panel
    public static final int ROWBORDERINIT = 30; // offset from top-border panel border towards bottom
    public static final int ROWADDENDUM = 4; // extra space for rows
    public static final int ROWBORDERDIFF = PERSONFONTSIZE + ROWADDENDUM; // height per row
    public static final int COLUMNBORDERINIT = 30; // offset from left border towards right
    public static final int COLUMNWIDTHADD = 12; // extra space for columns
    public static final int COLUMNWIDTHHALFADD = COLUMNWIDTHADD / 2; // half of extra column space
    public static final int UNITINCREMENT = 3*ROWBORDERDIFF; // offset for scrolling purposes
}
