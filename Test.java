package neurone;

public class Test {

    double[][] testData;
    double[] testResults;
    private NeuralNetwork nn;
    int nbErrors = 0;
    int accuracy = 0;



    Test(double[][] testData, double[] testResults) {
        this.testData = testData;
        this.testResults = testResults;
    }

    public void set(NeuralNetwork nn) {
        this.nn = nn;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void test() {
        if(nn == null) {
            System.out.println("Please enter the Neural Network that you want to test .\n Use the set(NeuralNework) " +
                    "method available in the Test class .");
            return;
        }
        for (int i = 0; i < testData.length; i++) {
            double a = nn.predict(testData[i]);
            if(a != Math.round(testResults[i]))
                nbErrors++;
        }
        accuracy = 100 - nbErrors * 100 / testData.length;

    }



}
