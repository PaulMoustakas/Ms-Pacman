package pacman.entries.pacman;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE> {

	private MOVE myMove=MOVE.NEUTRAL;

	private List<DataTuple> testSet;
	private List<DataTuple> trainingSet;
	private Map<String, List<String>> traitMap = new HashMap<String, List<String>>();

	private Node node;

	public MyPacMan() {
		divideAndGetDataPortions(20);



	}

	
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
		
		return myMove;
	}



	/**
	 * Divides the Data to training and testing sections.
	 * @Param trainingSetPortion represent the % of data to be used for training.
	 * */
	private void divideAndGetDataPortions(int trainingSetPortion) {
		trainingSet = new ArrayList<>();
		testSet = new ArrayList<>();

		List<DataTuple> data = new ArrayList<>(Arrays.asList(DataSaverLoader.LoadPacManData()));	//DATA
		int trainingData = (int) (data.size() * (trainingSetPortion * 0.01));						//PORTIONS
		Random random = new Random(new Date().getTime());

		// Training portion
		for (int i = 0; i < trainingData; i++) {
			int row = random.nextInt(data.size());
			trainingSet.add(data.get(row));
			data.remove(row);
		}

		// Test portion
		trainingSet.addAll(data);
	}








}