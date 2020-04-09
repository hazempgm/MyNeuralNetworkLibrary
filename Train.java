package neurone;

import neurone.NeuralNetwork;

public class Train {

    private byte trainSwitch = 0;

    private double[] data;
    private double results;

    private double[][] allData;
    private double[] allResults;
    private NeuralNetwork nn;

    Train(double[] data, double results) {
        this.data = data;
        this.results = results;
    }

    Train(double[][] allData, double[] allResults) {
        trainSwitch = 1;
        this.allData = allData;
        this.allResults = allResults;
    }

    public void set(NeuralNetwork nn) {
        this.nn = nn;
    }

    public void trainAll(int epoches) {

//        for (int i = 0; i < epoches; i++) {
//            int j = (int) (Math.random() * allResults.length);
//            nn.train(allData[j], allResults[j], i);
//        }
        for (int i = 0; i < allResults.length; i++) {
            int j = (int) (Math.random() * allResults.length);
            nn.train(allData[i], allResults[i], i);
        }

    }



    public void train(int i) {
        if(nn == null) {
            System.out.println("Please enter the Neural Network that you want to train .\n Use the set(NeuralNework) " +
                    "method available in the Train class .");
            return;
        } else if (trainSwitch == 1) {
            return;
        }
        nn.train(data, results, i);

    }
}
