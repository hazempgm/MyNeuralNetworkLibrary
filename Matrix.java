package neurone;

import java.util.Random;
import java.util.function.Function;

public class Matrix {
    public static Function<Double, Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
    public static Function<Double, Double> sigmoidDD = x -> sigmoid.apply(x)* (1 - sigmoid.apply(x)) ;

    public static Function<Double, Double> tanh = x -> Math.tanh(x);
    public static Function<Double, Double> tanhDD = x -> 1 - tanh.apply(x);

    public static Function<Double, Double> softSign = x -> x / (Math.abs(x) + 1);
    public static Function<Double, Double> softSignDD = x -> 1 / Math.pow((Math.abs(x) + 1), 2);

    public static Function<Double, Double> sign = x -> Double.valueOf(x >= 0?1:-1);

    Random random = new Random(2);

    private double[][] matrix;
    Matrix(int rows, int cols) {
        matrix = new double[rows][cols];

    }

    Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    Matrix(double[] matrix) {
        double[][] result = new double[matrix.length][1];
        for (int i = 0; i < result.length; i++)
            result[i][0] = matrix[i];

        this.matrix = result;
    }
    public Matrix randomize() {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = Math.random()* 2 - 1;
        return this;
    }



    public Matrix add(double s) {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = s + matrix[j][i];

        return new Matrix(result);

    }



    public Matrix scaler(double s) {
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = s * matrix[i][j];

        return new Matrix(result);
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
        return new Matrix(matrix1).multiply(new Matrix(matrix2)).getMatrix();
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix.getMatrix();
    }

    public void setMatrix(double[][] array) {
        matrix = array;

    }


    public Matrix transpose() {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = matrix[j][i];
        return new Matrix(result);
    }

    public Matrix applyFunction(Function<Double, Double> function) {
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = function.apply(matrix[i][j]);
        return new Matrix(result);
    }

    public static Matrix mSubstract(Matrix matrix1, Matrix matrix2) {
        double[][] result = new double[matrix1.getMatrix().length][matrix1.getMatrix()[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[i].length; j++)
                result[i][j] = matrix1.getMatrix()[i][j] - matrix2.getMatrix()[i][j];
        return new Matrix(result);
    }

    public static Matrix substract(Matrix matrix, double s, int m) {
        double[][] result = new double[matrix.getMatrix().length][matrix.getMatrix()[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[i].length; j++)
                result[i][j] = (matrix.getMatrix()[i][j] - s) * m;
        return new Matrix(result);
    }

    public Matrix multiply(Matrix matrix) {
        double[][] result = new double[this.matrix.length][matrix.getMatrix()[0].length];
//        System.out.println("First Matrix -> row: " + this.matrix.length + ", col: " + this.matrix[0].length);
//        System.out.println("Second Matrix -> row: " + matrix.getMatrix().length + ", col: " + matrix.getMatrix()[0].length);

        for (int i = 0; i < this.matrix.length; i++) {

            for (int k = 0; k < matrix.getMatrix()[0].length; k++) {

                double sum = 0;
                for (int j = 0; j < matrix.getMatrix().length; j++)
                    sum += this.matrix[i][j] * matrix.getMatrix()[j][k];
                    result[i][k] = sum;
                }
            }
//        System.out.println("Result -> row: " + result.length + ", col: " + result[0].length);

        return new Matrix(result);
    }

    public Matrix specialMultiply(Matrix matrix) {
        double[][] result = new double[this.matrix.length][this.matrix[0].length];
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                result[i][j] = this.matrix[i][j] * matrix.getMatrix()[i][j];
            }
        }

        return new Matrix(result);
    }


    public double[] flatten() {
        double[] result = new double[matrix.length * matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i * matrix.length + j * matrix[0].length] = matrix[i][j];
        return result;
    }

    public Matrix mAdd(Matrix matrix)  {
//        System.out.println("Add !! First Matrix -> row: " + this.matrix.length + ", col: " + this.matrix[0].length);
//        System.out.println("Second Matrix -> row: " + matrix.getMatrix().length + ", col: " + matrix.getMatrix()[0].length);

        double[][] result = new double[matrix.getMatrix().length][matrix.getMatrix()[0].length];
            for (int i = 0; i < this.matrix.length; i++)
                for (int j = 0; j < this.matrix[i].length; j++)
                    result[i][j] = this.matrix[i][j] + matrix.getMatrix()[i][j];

        return new Matrix(result);
    }

    public String toString() {
        String result = "[";
        for (int i = 0; i < matrix.length; i++) {
            result += "[" + matrix[i][0];
            for (int j = 1; j < matrix[0].length; j++)
                result += " ," + matrix[i][j];
            result += "] ,";
        }
        result += "]";
        return result;
    }
}
