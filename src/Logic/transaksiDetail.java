package Logic;

public class transaksiDetail {
    private String menuId;
    private int jumlah;
    private double subtotal;

    public transaksiDetail(String menuId, int jumlah, double subtotal) {
        this.menuId = menuId;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public String getMenuId() { return menuId; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal() { return subtotal; }
}
