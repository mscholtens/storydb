package core;

import model.StoryModel;
import view.StoryFrame;

/**
 * Created by Marijn on 23.12.17.
 */
public class StoryDB {

    private StoryFrame storyFrame;
    private StoryModel storyModel;

    public static void main(String[] args) {
        new StoryDB();
    }

    private StoryDB() {
        storyModel = new StoryModel();
        storyFrame = new StoryFrame(storyModel);
    }
}
