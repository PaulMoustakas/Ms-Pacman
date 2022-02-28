package pacman.entries.pacman;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

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
	private Map<String, List<String>> traitMap;
	private List<String> traitList;

	private Node rootNode;

	public MyPacMan() {
		divideAndGetDataPortions(20);
		initializeModelAttributes();


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
		testSet.addAll(data);
	}


	private pacman.entries.pacman.Node initializeTree (ArrayList<DataTuple> trainingSet, ArrayList<String> traitList) {

		pacman.entries.pacman.Node node = new pacman.entries.pacman.Node();

		MOVE move = trainingSet.get(0).DirectionChosen;
		boolean classDifference = false;
		int i = 0;

		while (i < trainingSet.size()) {
			if (trainingSet.get(i).DirectionChosen != move) {
				classDifference = true;
				i = trainingSet.size();
			}
			i++;
		}

		if (!classDifference) {
			node.setLabel(move.toString());
			return node;
		}

		if (traitList.size() == 0) {
			node.setLabel(getMajority(trainingSet).toString());
			return node;
		}

	}

	public void treeAccuracy () {
		double upMovesPerformed = 0, downMovesPerformed = 0, leftMovesPerformed = 0, rightMovesPerformed = 0, neutralMovesPerformed = 0;

		double modelHits = 0;

		int i = 0;

		while (i < testSet.size()) {
			MOVE testSetMove = testSet.get(i).DirectionChosen;
			MOVE actualMove = treeTraversal(rootNode,testSet.get(i));

			if (testSetMove == actualMove)
				modelHits++;

			if (testSetMove == MOVE.UP)
				upMovesPerformed++;

			if (testSetMove == MOVE.DOWN)
				downMovesPerformed++;


			if (testSetMove == MOVE.LEFT)
				leftMovesPerformed++;

			if (testSetMove == MOVE.NEUTRAL)
				neutralMovesPerformed++;


			double accuracy = modelHits / testSet.size();

			System.out.println("\nTree Accuracy: " + accuracy + "\n");

			System.out.println("Up: " + (upMovesPerformed/ testSet.size()));
			System.out.println("Down: " + (downMovesPerformed / testSet.size()));
			System.out.println("Left: " + (leftMovesPerformed / testSet.size()));
			System.out.println("Right: " + ( rightMovesPerformed/ testSet.size()));
			System.out.println("Neutral: " + (neutralMovesPerformed / testSet.size()));

		}
	}

	private MOVE treeTraversal(pacman.entries.pacman.Node node, DataTuple dataTuple) {

		MOVE move;

		if (node.nodeChildren.size() == 0 ) {
			 move = MOVE.valueOf(node.getNodeLabel());
		}

		else {
			String valueOfAttribute = dataTuple.returnState(node.getNodeLabel());
			pacman.entries.pacman.Node next = node.getChild(valueOfAttribute);

			move = treeTraversal(next,dataTuple);
		}

		return move;
	}




	private void initializeModelAttributes() {
		traitMap = new HashMap<>();

		setDirections();
		setDistance();
		setBools();

		traitList = new ArrayList<>(traitMap.keySet());
	}

	private void setDirections () {
		List<String> directionStrings = new ArrayList<>();

		directionStrings.add("UP");
		directionStrings.add("DOWN");
		directionStrings.add("RIGHT");
		directionStrings.add("NEUTRAL");
		directionStrings.add("LEFT");

		traitMap.put("inkyDir", directionStrings);
		traitMap.put("pinkyDir", directionStrings);
		traitMap.put("blinkyDir", directionStrings);
		traitMap.put("sueDir", directionStrings);
	}

	private void setDistance() {
		List<String> distanceStrings = new ArrayList<>();

		distanceStrings.add("VERY_HIGH");
		distanceStrings.add("HIGH");
		distanceStrings.add("MEDIUM");
		distanceStrings.add("LOW");
		distanceStrings.add("VERY_LOW");
		distanceStrings.add("NONE");

		traitMap.put("blinkyDist", distanceStrings);
		traitMap.put("pinkyDist", distanceStrings);
		traitMap.put("sueDist", distanceStrings);
		traitMap.put("inkyDist", distanceStrings);
	}

	private void setBools () {
		List<String> boolString = new ArrayList<>();

		boolString.add("true");
		boolString.add("false");

		traitMap.put("isBlinkyEdible",boolString);
		traitMap.put("isPinkyEdible",boolString);
		traitMap.put("isSueEdible",boolString);
		traitMap.put("isInkyEdible",boolString);
	}


	private MOVE getMajority (ArrayList<DataTuple> data) {

		List<MoveDirection> moveDirectionList = new ArrayList();
		moveDirectionList.add(new MoveDirection(MOVE.UP));
		moveDirectionList.add(new MoveDirection(MOVE.DOWN));
		moveDirectionList.add(new MoveDirection(MOVE.LEFT));
		moveDirectionList.add(new MoveDirection(MOVE.RIGHT));
		moveDirectionList.add(new MoveDirection(MOVE.NEUTRAL));

		for (DataTuple dataTuple : data ) {
			if (dataTuple.DirectionChosen == MOVE.UP)
				moveDirectionList.get(0).incrementMoveCounter();

			if (dataTuple.DirectionChosen == MOVE.DOWN)
				moveDirectionList.get(1).incrementMoveCounter();

			if (dataTuple.DirectionChosen == MOVE.LEFT)
				moveDirectionList.get(2).incrementMoveCounter();

			if (dataTuple.DirectionChosen == MOVE.RIGHT)
				moveDirectionList.get(3).incrementMoveCounter();

			if (dataTuple.DirectionChosen == MOVE.NEUTRAL)
				moveDirectionList.get(4).incrementMoveCounter();
		}

		moveDirectionList.sort(Comparator.comparing(MoveDirection::getMoveCounter));

		return moveDirectionList.get(4).move;
	}



	public static class MoveDirection {

		public MOVE move;
		public int moveCounter;


		public MoveDirection (MOVE move) {
			this.move = move;
			moveCounter = 0;
		}

		public MOVE getMove() {
			return move;
		}

		public int getMoveCounter() {
			return moveCounter;
		}

		public void incrementMoveCounter () {
			moveCounter++;
		}
	}






}