package supermercado.model;

public enum PerfilUsuario {
    ADMINISTRADOR("Administrador"),
    ATENDENTE("Atendente"),
    CLIENTE("Cliente");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
