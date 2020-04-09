package neurone;

class DifferentMatrixDimesionException extends Exception {
    DifferentMatrixDimesionException() {
        super();
    }
    DifferentMatrixDimesionException(String string)  {
        super(string);
    }

    DifferentMatrixDimesionException(double rowF, double colF, double rowE, double colE)  {
        super(String.format("Expected row: {}, col: {}\nBut found row: {}, col: {}", rowE, colE, rowF, colF));
    }
}
