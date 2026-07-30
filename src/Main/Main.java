package Main;

import Util.Console;
import Util.Menu;

public class Main {

    public static void main(String[] args) {
        Console.limpar();
        System.out.println("============================================");
        System.out.println("   INICIALIZANDO O SISTEMA STOCKFLOW...     ");
        System.out.println("============================================");

        Menu menu = new Menu();
        menu.iniciar();
    }
}
