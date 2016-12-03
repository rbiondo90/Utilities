/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robertobiondo.utilities;

/**
 *
 * @author Roberto Biondo
 */
public class IllegalDateException extends Exception {

    public IllegalDateException(String message) {
        super(message);
    }

    public IllegalDateException() {
        this("Invalid Date!");
    }

    @Override
    public String toString() {
        return ("MyIllegalDateException: " + this.getMessage());
    }
}
