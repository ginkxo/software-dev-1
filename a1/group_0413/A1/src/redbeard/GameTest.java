package redbeard;

import heap.HeapEmptyException;
import heap.HeapFullException;
import world.Node;

import java.io.FileNotFoundException;
import java.io.IOException;

//TODO: TESTING!

// This class is not part of your evaluation. You may use it for testing if you want.
public class GameTest {

    /**
     * @param args
     * @throws HeapEmptyException
     * @throws HeapFullException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException, HeapFullException, HeapEmptyException {
        // TODO Auto-generated method stub
        TreasureHunt game = new TreasureHunt(10, 30, 15, 3, 500);
//        TreasureHunt game2 = new TreasureHunt();
//        System.out.println("Boat Coordinates:");
//        System.out.println(game.islands.getBoat().gridX);
//        System.out.println(game.islands.getBoat().gridY);

        game.play("src/game.txt");
//        System.out.println(game.getMap());
        System.out.println(game.state);
        System.out.println(game.pathLength());

//        for (Node coord : game.islands.getNeighbours(game.islands.getBoat())) {
//            System.out.println(coord.gridX + " " + coord.gridY);
//        }


//        System.out.println(game.getMap());


    }


}
