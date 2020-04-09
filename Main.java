package neurone;


import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static void print(Object string) {
        System.out.print("" + string);
    }

    static void println(Object string) {
        System.out.println("" + string);
    }

    static double[][] trainingData;
    static double[] results;

    static Path fiveKTrain = Path.of("src/datasets/5000-per-digit_labels_train");
    static Path fiveKLabel = Path.of("src/datasets/5000-per-digit_labels_train");
    static Path train = Path.of("/home/xenospgm/numberClassifier/train/train-images.idx3-ubyte");
    static Path test = Path.of("/home/xenospgm/numberClassifier/train/train-labels.idx1-ubyte");

    static double[][] testData;
    static double[] testResults;


    public static void main(String[] args) throws DifferentMatrixDimesionException {

        byte[] file = NeuralNetwork.readFile(train);
        byte[] file1 = NeuralNetwork.readFile(test);

        byte[] test = NeuralNetwork.readFile(Path.of("/home/xenospgm/numberClassifier/test/t10k-images.idx3-ubyte"));
        byte[] test1 = NeuralNetwork.readFile(Path.of("/home/xenospgm/numberClassifier/test/t10k-labels.idx1-ubyte"));

// ---------------------------------------------------------------------------------------------------------------

        int testLabelMagic = NeuralNetwork.parseInt(test1, 0);
        int testNbResults = NeuralNetwork.parseInt(test1, 4);
        int startTestLabel = 8;
        testResults = new double[testNbResults];
// ---------------------------------------------------------------------------------------------------------------

        int testTrainingMagic = NeuralNetwork.parseInt(test, 0);
        int testNbImages = NeuralNetwork.parseInt(test, 4);
        int testRows = NeuralNetwork.parseInt(test, 8);
        int testCols = NeuralNetwork.parseInt(test, 12);
        int testStartTrain = 16;
        testData = new double[testNbImages][testRows * testCols];



// ---------------------------------------------------------------------------------------------------------------

        int labelsMagic = NeuralNetwork.parseInt(file1, 0);
        int nbResult = NeuralNetwork.parseInt(file1, 4);
        int startLabel = 8;
        results = new double[nbResult];
// ---------------------------------------------------------------------------------------------------------------

        int trainingMagic = NeuralNetwork.parseInt(file, 0);
        int numberImages = NeuralNetwork.parseInt(file, 4);
        int rows = NeuralNetwork.parseInt(file, 8);
        int cols = NeuralNetwork.parseInt(file, 12);
        int startTrain = 16;
        trainingData = new double[numberImages][rows * cols];


// ---------------------------------------------------------------------------------------------------------------

        println("Magic: " + trainingMagic + ", Number of images: " + numberImages + ", rows: " + rows + ", cols: " + cols);


// ---------------------------------------------------------------------------------------------------------------

        int j = -1;
        int k = 0;
        for (int i = 0; i < file.length - startTrain; i++) {
            k = i % (rows * cols);
            j = k == 0?++j:j;
            trainingData[j][k] = NeuralNetwork.convertToInt(file[i + startTrain]);
        }
// ---------------------------------------------------------------------------------------------------------------

        for (int i = startLabel; i < nbResult; i++)
            results[i - startLabel] = file1[i];

// ---------------------------------------------------------------------------------------------------------------

        j = -1;
        k = 0;
        for (int i = 0; i < test.length - testStartTrain; i++) {
            k = i % (testRows * testCols);
            j = k == 0?++j:j;
            testData[j][k] = NeuralNetwork.convertToInt(file[i + startTrain]);
        }
// ---------------------------------------------------------------------------------------------------------------

        for (int i = startTestLabel; i < testNbResults; i++)
            testResults[i - startTestLabel] = file1[i];




// ---------------------------------------------------------------------------------------------------------------


        NeuralNetwork nn = new NeuralNetwork(rows * cols,  16, 16, 10);
//        nn.train(new double[] {0, 0}, 1, 1);
        Train train = new Train(trainingData, results);
        train.set(nn);
        train.trainAll(200000);
// ---------------------------------------------------------------------------------------------------------------

        Test myTest = new Test(testData, testResults);
        myTest.set(nn);
        myTest.test();
// ---------------------------------------------------------------------------------------------------------------


        println("The accuracy is :" + myTest.getAccuracy());
        println("Wait here");

    }
}