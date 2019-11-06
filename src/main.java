public class main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.menu();
        Client client = new Client(menu.getHost(), menu.getPort(), menu.getName());
        client.client();
    }
}