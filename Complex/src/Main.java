import com.complex.Complex;
import com.complex.Matrix;

public class Main {
    public static void main(String[] args) {

        /*Complex complex = new Complex(1, 2);
        Complex complex1 = new Complex(2, 3);

        System.out.println(complex.sum(complex1));
        System.out.println(complex.mul(complex1));
        System.out.println(complex.printGeom());*/

        Complex[][] test = new Complex[3][3];
        Complex[][] test1 = new Complex[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                test[i][j] =  new Complex(i+j, 0);
                test1[i][j] =  new Complex(i+j, 0);
            }
        }

        Matrix matrix = new Matrix(test);
        Matrix matrix1 = new Matrix(test1);

        Matrix sum = matrix.sum(matrix1);
        Matrix mul = matrix.mul(matrix1);
        Matrix transp = matrix.transp();

        System.out.println(matrix);
        System.out.println(matrix1);

        System.out.println(sum);
        System.out.println(mul);
        System.out.println(transp);


    }
}
