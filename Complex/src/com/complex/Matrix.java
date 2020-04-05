package com.complex;

import com.complex.Complex;

public class Matrix {

    private int col;
    private int row;
    private Complex[][] matrix;

    public Matrix(Complex[][] matrix) {

        this.row = matrix[0].length;
        this.col = matrix.length;

        this.matrix = new Complex[this.row][this.col];
        this.clone(matrix);

    }

    public void clone(Complex[][] matrix) {


        for (int i=0; i < this.row; ++i){
            if (this.col >= 0) System.arraycopy(matrix[i], 0, this.matrix[i], 0, this.col);
        }

    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Matrix sum(Matrix right_operand){

        try {
            Complex[][] sum_matrix = new Complex[this.row][this.col];
            for (int i = 0; i < this.row; ++i) {
                for (int j = 0; j < this.col; ++j) {
                    sum_matrix[i][j] = this.matrix[i][j].sum(right_operand.matrix[i][j]);
                }
            }

            return new Matrix(sum_matrix);

        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("different size");
        }

        return null;
    }

    public  Matrix mul(Matrix right_operand){

        if (this.getCol() != right_operand.getRow()){
            System.out.println("Unavailable sizes");
            return null;
        }

        Complex[][] mul_matrix = new Complex[this.col][this.row];

        for (int i = 0; i < this.row; ++i){
            for (int j = 0; j < right_operand.col; ++j){
                for (int k = 0; k < this.col; k++) {
                    if (mul_matrix[i][j] == null){
                        mul_matrix[i][j] = this.matrix[i][k].mul(right_operand.matrix[k][j]);
                    }else{
                        mul_matrix[i][j] = mul_matrix[i][j].sum(this.matrix[i][k].mul(right_operand.matrix[k][j]));

                    }
                }
            }
        }

        return new Matrix(mul_matrix);

    }

    public Matrix transp(){

        Complex[][] transp_matrix = new Complex[this.col][this.row];

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                transp_matrix[j][i] = this.matrix[i][j];
            }
        }

        return  new Matrix(transp_matrix);

    }

    public String toString(){

        StringBuilder res = new StringBuilder();



        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                res.append(this.matrix[i][j]);
                res.append(" ");
            }
            res.append("\n");
        }

        return res.toString();
    }
}

/*
* 1 1 1 | 2 2
* 1 1 1 | 2 2
* 1 1 1 | 2 2
*  */
