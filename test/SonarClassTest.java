import edu.colorado.fantasticfour.game.Game;
import edu.colorado.fantasticfour.game.Player;
import edu.colorado.fantasticfour.location.Location;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class SonarClassTest {
    private Player player;
    private Player player2;
    private Game game;
    private boolean result;
    private boolean[] resultList = new boolean[13];
    private Location target;
    private Location location1;
    private Location location2;


    @Before
    public void setUp(){
        game = new Game();
        player = game.getPlayer("1");
        player2 = game.getPlayer("2");
        target = new Location(5,5);
        location1 = new Location(5,4);
        location2 = new Location(6, 4);
    }

    @Test
    public void sonarObjectExists() {
        Assert.assertNotNull(player.getSonar());
    }

    @Test
    public void startWithMovesRemaining() {
        Assert.assertEquals(2, player.getSonar().movesRemain());
    }

    @Test
    public void decrementsMoveCount() {
        // place and hit an opponent ship
        player2.placeShip("Minesweeper", new Location(0,0), "W");
        player.takeShot(new Location(0,0));
        player.getSonar().useAt(target);
        Assert.assertEquals(1, player.getSonar().movesRemain());
    }

    @Test
    public void noSonarLeft() {
        // place and hit an opponent ship
        player2.placeShip("Minesweeper", new Location(0,0), "W");
        player.takeShot(new Location(0,0));
        player.getSonar().useAt(target);
        Assert.assertEquals(1, player.getSonar().movesRemain());
        player.getSonar().useAt(target);
        Assert.assertEquals(0, player.getSonar().movesRemain());
        try{
            player.getSonar().useAt(target);
            fail();
        }catch (IllegalArgumentException e){
            Assert.assertEquals("No moves left", e.getMessage());
        }
    }

    @Test
    public void haveNotSunkShip(){
        // attempt to use sonar without sinking opponent ship first
        try{
            player.getSonar().useAt(target);
        }catch (IllegalArgumentException e){
            Assert.assertTrue(e.getMessage().contains("Have not sunk opponent ship yet"));
        }
    }

    @Test
    public void sonarDetectsShip() {
        result = player.getSonar().getSonarAt(new Location(5,5));
        Assert.assertFalse(result);
    }

    @Test
    public void checkSonarResults() {
        // place and hit an opponent ship
        player2.placeShip("Destroyer", new Location(0,1), "N");
        player.takeShot(new Location(0,1));
        player.takeShot(new Location(0,1));
        // now I can use sonar
        player2.placeShip("Minesweeper", location1, "W");
        player.getSonar().useAt(target);
        resultList = player.getSonar().getSonarResults();
        for (int i = 0; i < resultList.length; i++){
            // added == 4 condition to verify process, not permanent
            if ((i == 2) || (i == 3)){
                Assert.assertTrue(resultList[i]);
            }else {
                Assert.assertFalse(resultList[i]);
            }
        }
    }

    @Test
    public void checkTargetInBounds() {
        try{
            player.getSonar().setTarget(new Location(11,13));
            fail();
        }catch (IllegalArgumentException e) {
            Assert.assertEquals("Location does not exist on this board", e.getMessage());
        }
    }

    @Test
    public void checkOutOfBoundsIsFalse() {
        // place and hit an opponent ship
        player2.placeShip("Minesweeper", new Location(9,9), "E");
        player.takeShot(new Location(9,9));
        player.getSonar().setTarget(new Location(0,0));
        player.getSonar().useAt(target);
        resultList = player.getSonar().getSonarResults();

        Assert.assertFalse(resultList[0]);
    }


}