package neurone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NeuralNetwork {

    private int numberInputNodes;
    private int nunberHiddenNodes;
    private int numberOutputNodes;

    private int layer = 1;

    static double lr = 0.75;
    Matrix firstWeights;
    Matrix WEIGHTS;
    Matrix lastWeights;

    Matrix hiddenBias;
    Matrix BIAS;
    Matrix outputBias;



    Matrix inputs;
    Matrix hiddenResult;
    Matrix HIDDEN;
    Matrix outputResult;

    NeuralNetwork(int inputs, int hidden,int hidden2, int output) {
        numberInputNodes = inputs;
        nunberHiddenNodes = hidden;
        numberOutputNodes = output;
        layer = 2;

        this.inputs = new Matrix(inputs, 1);
        firstWeights = new Matrix(hidden, inputs).randomize();
        hiddenBias = new Matrix(hidden, 1).randomize();
        WEIGHTS = new Matrix(hidden2, hidden).randomize();
        BIAS = new Matrix(hidden2, 1).randomize();
        lastWeights = new Matrix(output, hidden2).randomize();
        outputBias = new Matrix(output, 1).randomize();
    }

    NeuralNetwork(int inputs, int hidden, int output) {
        numberInputNodes = inputs;
        nunberHiddenNodes = hidden;
        numberOutputNodes = output;

        this.inputs = new Matrix(inputs, 1);
        firstWeights = new Matrix(hidden, inputs).randomize();
        hiddenBias = new Matrix(hidden, 1).randomize();
        lastWeights = new Matrix(output, hidden).randomize();
        outputBias = new Matrix(output, 1).randomize();
    }

    public int getMax() {
        double max = outputResult.getMatrix()[0][0];
        int pos = 0;
        for(int i = 0; i < outputResult.getMatrix().length; i++)
            if (max < outputResult.getMatrix()[i][0]) {
                max = outputResult.getMatrix()[i][0];
                pos = i;
            }
        return pos;

    }

    public int predict(double[] inputs) {
        feedforward(inputs);
        return getMax();
    }

    public Matrix feedforward(double[] array) {
        inputs = new Matrix(array);
        hiddenResult = firstWeights.multiply(inputs).mAdd(hiddenBias).applyFunction(Matrix.sigmoid);
        if (layer == 1) {
            outputResult = lastWeights.multiply(hiddenResult).mAdd(outputBias).applyFunction(Matrix.sigmoid);
            return outputResult;
        }
        HIDDEN = WEIGHTS.multiply(hiddenResult).mAdd(BIAS).applyFunction(Matrix.sigmoid);
        outputResult = lastWeights.multiply(HIDDEN).mAdd(outputBias).applyFunction(Matrix.sigmoid);
        return outputResult;

    }

    public static int parseInt(byte[] file, int pos) {
        return convertToInt(file[pos]) * (1 << 24) + convertToInt(file[pos + 1]) * (1 << 16)
                +  convertToInt(file[pos + 2]) * (1 << 8) + convertToInt(file[pos + 3]);
    }


    public static int convertToInt(byte b) {
        if (b >= 0)
            return b;
        return 256 + b;
    }

    public void train(double[] inputs, double answer, int k) {
        feedforward(inputs);
        // error = answers - output
        System.out.println("epoche: " + k);
//        Matrix errors = Matrix.mSubstract(new Matrix(new double[] {0, 1}), outputResult);
        Matrix errors = Matrix.substract(outputResult, 0, -1);
        errors.getMatrix()[(int) answer][0] += 1;

        if (layer == 1) {
            Matrix hidden_errors = lastWeights.transpose().multiply(errors);
            Matrix gradient_hidden = errors.specialMultiply(outputResult.applyFunction(Matrix.sigmoidDD)).scaler(lr);
            Matrix var1 = lastWeights.mAdd(gradient_hidden.multiply(hiddenResult.transpose()));
            lastWeights.setMatrix(var1);

            Matrix gradient_input = hidden_errors.specialMultiply(hiddenResult.applyFunction(Matrix.sigmoidDD)).scaler(lr);
//        System.out.println("This this the input: " + this.inputs);
//        System.out.println("This this gradient: " + gradient_hidden);

            Matrix var = gradient_input.multiply(this.inputs.transpose());
            firstWeights.setMatrix(firstWeights.mAdd(var));
            outputBias.setMatrix(outputBias.mAdd(gradient_hidden));
            hiddenBias.setMatrix(hiddenBias.mAdd(gradient_input));
            return;
        }
        Matrix HIDDEN_ERROS = lastWeights.transpose().multiply(errors);
        Matrix hidden_errors = WEIGHTS.transpose().multiply(HIDDEN_ERROS);
        Matrix GRADIENT_HIDDEN = errors.specialMultiply(outputResult.applyFunction(Matrix.sigmoidDD)).scaler(lr);
        Matrix VAR1 = lastWeights.mAdd(GRADIENT_HIDDEN.multiply(HIDDEN.transpose()));
        lastWeights.setMatrix(VAR1);

        Matrix gradient_hidden = HIDDEN_ERROS.specialMultiply(HIDDEN.applyFunction(Matrix.sigmoidDD)).scaler(lr);
        Matrix VAR = gradient_hidden.multiply(hiddenResult.transpose());
        WEIGHTS.setMatrix(WEIGHTS.mAdd(VAR));

//        Matrix hidden_errors = lastWeights.transpose().multiply(errors);
//        Matrix gradient_hidden = errors.specialMultiply(outputResult.applyFunction(Matrix.sigmoidDD)).scaler(lr);
//        var var1 = lastWeights.mAdd(hiddenResult.multiply(gradient_hidden.transpose()).transpose());
//        lastWeights.setMatrix(var1);

        Matrix gradient_input = hidden_errors.specialMultiply(hiddenResult.applyFunction(Matrix.sigmoidDD)).scaler(lr);
//        System.out.println("This this the input: " + this.inputs);
//        System.out.println("This this gradient: " + gradient_hidden);

        Matrix var = gradient_input.multiply(this.inputs.transpose());

        firstWeights.setMatrix(firstWeights.mAdd(var));
        BIAS.setMatrix(BIAS.mAdd(gradient_hidden));
        outputBias.setMatrix(outputBias.mAdd(GRADIENT_HIDDEN));
        hiddenBias.setMatrix(hiddenBias.mAdd(gradient_input));

    }


    public static byte[] readFile(Path path) {
        byte[] file = null;
        try {
            if(!Files.isRegularFile(path))
                throw new IrregularFileException();
            file = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println("Please check file permission .");
        } catch (IrregularFileException e) {
            System.out.println("There were problem with the file, check for correct path .");
        }
        return file;
    }

}
