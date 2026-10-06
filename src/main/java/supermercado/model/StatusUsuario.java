package supermercado.model;

public enum StatusUsuario {
    HABILITADO("Habilitado"),
    DESABILITADO("Desabilitado");

    private final String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
