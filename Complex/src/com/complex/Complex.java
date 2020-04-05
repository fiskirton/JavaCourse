package com.complex;

public class Complex {

    private double real;
    private double imag;

    public Complex(double real, double imag){

        this.real = real;
        this.imag = imag;

    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public Complex sum(Complex right_operand){

        double res_real = this.real + right_operand.real;
        double res_imag = this.imag + right_operand.imag;

        return new Complex(res_real, res_imag);

    }



    public Complex mul(Complex right_operand){

        double res_real = this.real * right_operand.real - this.imag * right_operand.imag;
        double res_imag = this.real * right_operand.imag + this.imag * right_operand.real;

        return new Complex(res_real, res_imag);

    }

    public String printGeom(){

        double r = Math.sqrt(this.real * this.real + this.imag * this.imag);
        double phi = Math.acos(Math.cos(this.real / r));

        String format_string = "%.2f(cos(%2$.2f)+isin(%2$.2f))";

        return String.format(format_string, r, phi);

    }
    public String toString(){

        String format_string;

        if (this.imag == 0){
            format_string = "%.2f";
            return String.format(format_string, this.real);
        }else {
            format_string = "%.2f%+.2fi";
            return String.format(format_string, this.real, this.imag);
        }

    }
}
