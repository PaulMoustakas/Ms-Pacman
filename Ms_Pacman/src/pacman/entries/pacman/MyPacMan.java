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

	private final MOVE myMove=MOVE.NEUTRAL;

	private List<DataTuple> testSet;
	private List<DataTuple> trainingSet;
	private Map<String, List<String>> attributeMap;
	private ArrayList<String> attributeList;

	private final Node rootNode;

	public MyPacMan() {
		divideAndGetDataPortions(20);
		initializeModelAttributes();
		rootNode = initializeTree((ArrayList<DataTuple>) trainingSet, attributeList);
<<<<<<< Updated upstream
		// treeAccuracy();
=======
		treeAccuracy();
>>>>>>> Stashed changes
	}

	
	public MOVE getMove(Game game, long timeDue) {
		return treeTraversal(rootNode, new DataTuple(game, null));
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



	public void treeAccuracy () {
		double upMovesPerformed = 0, downMovesPerformed = 0, leftMovesPerformed = 0, rightMovesPerformed = 0, neutralMovesPerformed = 0;

		double modelHits = 0;

		for (DataTuple d: testSet) {
			MOVE testSetMove = d.DirectionChosen;
			MOVE actualMove = treeTraversal(rootNode, d);

			if(testSetMove == actualMove) {
				modelHits++;
<<<<<<< Updated upstream
			if (testSetMove == MOVE.UP)
				upMovesPerformed++;
			if (testSetMove == MOVE.DOWN)
				downMovesPerformed++;
			if (testSetMove == MOVE.LEFT)
				leftMovesPerformed++;
			if (testSetMove == MOVE.NEUTRAL)
				neutralMovesPerformed++;

			double accuracy = modelHits / testSet.size();
			System.out.println("Tree Accuracy: " + accuracy + "\n");
		}
=======
			}

			switch (testSetMove) {
				case UP -> upMovesPerformed++;
				case DOWN -> downMovesPerformed++;
				case LEFT -> leftMovesPerformed++;
				case RIGHT -> rightMovesPerformed++;
				case NEUTRAL -> neutralMovesPerformed++;
			}
		}


		double accuracy = modelHits / testSet.size();

			System.out.println("Tree Accuracy: " + accuracy + "\n");
			System.out.println("Amount of up-moves performed: " + (upMovesPerformed / testSet.size()));
			System.out.println("Amount of down-moves performed: " + (downMovesPerformed / testSet.size()));
			System.out.println("Amount of left-moves performed: " + (leftMovesPerformed / testSet.size()));
			System.out.println("Amount of right-moves performed: " + (rightMovesPerformed/ testSet.size()));
			System.out.println("Amount of neutral moves performed: " + (neutralMovesPerformed / testSet.size()));

>>>>>>> Stashed changes
	}

	private MOVE treeTraversal(pacman.entries.pacman.Node node, DataTuple dataTuple) {

		MOVE move;

		if (node.nodeChildren.size() == 0 ) {
			 move = MOVE.valueOf(node.getLabel());
		}
		else {
			String valueOfAttribute = dataTuple.returnState(node.getLabel());
			Node next = node.getChild(valueOfAttribute);
			move = treeTraversal(next,dataTuple);
		}
		return move;
	}



	private void initializeModelAttributes() {
		attributeMap = new HashMap<>();
		setDirections();
		setDistance();
		setBools();
		attributeList = new ArrayList<>(attributeMap.keySet());
	}

	private void setDirections () {
		List<String> directionStrings = new ArrayList<>();

		directionStrings.add("UP");
		directionStrings.add("DOWN");
		directionStrings.add("RIGHT");
		directionStrings.add("NEUTRAL");
		directionStrings.add("LEFT");

		attributeMap.put("inkyDir", directionStrings);
		attributeMap.put("pinkyDir", directionStrings);
		attributeMap.put("blinkyDir", directionStrings);
		attributeMap.put("sueDir", directionStrings);
	}

	private void setDistance() {
		List<String> distanceStrings = new ArrayList<>();

		distanceStrings.add("VERY_HIGH");
		distanceStrings.add("HIGH");
		distanceStrings.add("MEDIUM");
		distanceStrings.add("LOW");
		distanceStrings.add("VERY_LOW");
		distanceStrings.add("NONE");

		attributeMap.put("blinkyDist", distanceStrings);
		attributeMap.put("pinkyDist", distanceStrings);
		attributeMap.put("sueDist", distanceStrings);
		attributeMap.put("inkyDist", distanceStrings);
	}

	private void setBools () {
		List<String> boolString = new ArrayList<>();

		boolString.add("true");
		boolString.add("false");

		attributeMap.put("isBlinkyEdible",boolString);
		attributeMap.put("isPinkyEdible",boolString);
		attributeMap.put("isSueEdible",boolString);
		attributeMap.put("isInkyEdible",boolString);

		attributeMap.put("atJunction",boolString);

		attributeMap.put("upMovePossible",boolString);
		attributeMap.put("downMovePossible",boolString);
		attributeMap.put("leftMovePossible",boolString);
		attributeMap.put("rightMovePossible",boolString);
	}


	private pacman.entries.pacman.Node initializeTree (ArrayList<DataTuple> trainingSet, ArrayList<String> attributeList) {

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

		if (attributeList.size() == 0) {
			node.setLabel(getMajority(trainingSet).toString());
			return node;
		}

		String A = returnS(trainingSet, attributeList);

		node.setLabel(A);
		attributeList.remove(A);

		List<String> possibleValueA = attributeMap.get(A);

		for (String AS : possibleValueA) {
			ArrayList<DataTuple> TS = new ArrayList<>();
			for (DataTuple dataTuple : trainingSet) {
				if (dataTuple.returnState(A).equals(AS)) {
					TS.add(dataTuple);
				}
			}

			if (TS.size() == 0) {
				node.addChild(AS,new pacman.entries.pacman.Node(getMajority(trainingSet).toString()));
			} else {
				node.addChild(AS,initializeTree(TS, (ArrayList<String>) attributeList.clone()));
			}
		}
		return node;
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

	/**
	 * ID3 Algorithm
	 * @Param dataset
	 * @Param attributes
	 * @retun S
	 */
	private String returnS(ArrayList<DataTuple> dataSet, ArrayList<String> traitList) {
		String returnValue = "";
		double valueAlpha = Integer.MAX_VALUE;

		for (String A : traitList) {
			double tempA = 0.0;
			ArrayList<String> possibleValueA = (ArrayList<String>) attributeMap.get(A);
			Map<String, Integer> valueMap = new HashMap<>();

			for (String AValue : possibleValueA) {
				ArrayList<DataTuple> subSet = new ArrayList<>();
				valueMap.put(AValue, 0);

				for (DataTuple dataTuple : dataSet) {
					if (dataTuple.returnState(A).equals(AValue)) {
						valueMap.put(AValue, valueMap.get(AValue) +1 );
						subSet.add(dataTuple);
					}
				}

				HashMap<MOVE, Integer> directionMoveCountMap = directionMoveCounterMap();

				for (DataTuple dataTuple : subSet) {
					if (dataTuple.DirectionChosen == MOVE.UP)		{ directionMoveCountMap.put(MOVE.UP, 		directionMoveCountMap.get(MOVE.UP) 		+1); }
					if (dataTuple.DirectionChosen == MOVE.DOWN) 	{ directionMoveCountMap.put(MOVE.DOWN, 		directionMoveCountMap.get(MOVE.DOWN) 	+1); }
					if (dataTuple.DirectionChosen == MOVE.LEFT) 	{ directionMoveCountMap.put(MOVE.LEFT, 		directionMoveCountMap.get(MOVE.LEFT) 	+1); }
					if (dataTuple.DirectionChosen == MOVE.RIGHT) 	{ directionMoveCountMap.put(MOVE.RIGHT, 	directionMoveCountMap.get(MOVE.RIGHT) 	+1); }
					if (dataTuple.DirectionChosen == MOVE.NEUTRAL) 	{ directionMoveCountMap.put(MOVE.NEUTRAL,	directionMoveCountMap.get(MOVE.NEUTRAL) +1); }
				}

				double valueMoveCount = valueMap.get(AValue);

				if (valueMoveCount != 0) {
					double AValueOccurrences = (valueMoveCount / dataSet.size());
					double subsetUP 	= (directionMoveCountMap.get(MOVE.UP) 		/ valueMoveCount) * (log2(directionMoveCountMap.get(MOVE.UP) 	  / valueMoveCount));
					double subsetDown 	= (directionMoveCountMap.get(MOVE.DOWN) 	/ valueMoveCount) * (log2(directionMoveCountMap.get(MOVE.DOWN) 	  / valueMoveCount));
					double subsetLeft	= (directionMoveCountMap.get(MOVE.LEFT)		/ valueMoveCount) * (log2(directionMoveCountMap.get(MOVE.LEFT) 	  / valueMoveCount));
					double subsetRight 	= (directionMoveCountMap.get(MOVE.RIGHT) 	/ valueMoveCount) * (log2(directionMoveCountMap.get(MOVE.RIGHT)	  / valueMoveCount));
					double subsetNeutral= (directionMoveCountMap.get(MOVE.NEUTRAL) 	/ valueMoveCount) * (log2(directionMoveCountMap.get(MOVE.NEUTRAL)   / valueMoveCount));

					tempA += AValueOccurrences * (- subsetUP - subsetDown - subsetLeft - subsetRight - subsetNeutral);
				}
			}

			if (tempA < valueAlpha) {
				valueAlpha = tempA;
				returnValue = A;
			}
		}
		return returnValue;
	}



	private HashMap<MOVE, Integer> directionMoveCounterMap () {
		HashMap<MOVE, Integer> directionMoveCountMap = new HashMap<>();
		directionMoveCountMap.put(MOVE.UP, 0);
		directionMoveCountMap.put(MOVE.DOWN, 0);
		directionMoveCountMap.put(MOVE.LEFT, 0);
		directionMoveCountMap.put(MOVE.RIGHT, 0);
		directionMoveCountMap.put(MOVE.NEUTRAL, 0);

		return directionMoveCountMap;
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


	private double log2 (double moveCountOverMoveValue) {
			if(moveCountOverMoveValue == 0) return 0;
			return (Math.log(moveCountOverMoveValue)/Math.log(2));
		}
	}

