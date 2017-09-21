package edu.byu.cs.superasteroids.model.positioned_objects.moving_objects;

import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Cannon;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.Engine;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.ExtraPart;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.MainBody;
import edu.byu.cs.superasteroids.model.positioned_objects.moving_objects.ship_parts.PowerCore;

/**
 * Created by tylerku on 10/20/16.
 *
 *  Constainer class for all the ship parts of any given ship
 */
public class ShipParts {

    Cannon cannon;
    Engine engine;
    ExtraPart extraPart;
    MainBody mainBody;
    PowerCore powerCore;

    ShipParts(Cannon c, Engine eng, ExtraPart ep, MainBody mb, PowerCore pc){
        cannon = c;
        engine = eng;
        extraPart = ep;
        mainBody = mb;
        powerCore = pc;
    }
}
