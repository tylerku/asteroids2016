package edu.byu.cs.superasteroids.model;

/**
 * Created by tylerku on 10/20/16.
 *
 * Any Object that is visible on screen
 */
public class VisibleObject {

    private String imageURL;

     /**
     *
     * @param URLimage - String holding the URL for the visibleObject image
     */

    public VisibleObject(String URLimage){
        imageURL = URLimage;
    }

    public String getImageURL(){
        return imageURL;
    }
}
