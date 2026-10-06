package supermercado;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;

public final class Componentes {

    private Componentes() {
    }

    public static <T extends Component> List<T> todos(Container raiz, Class<T> tipo) {
        List<T> encontrados = new ArrayList<>();
        coletar(raiz, tipo, encontrados);
        return encontrados;
    }

    public static JButton botao(Container raiz, String texto) {
        return todos(raiz, JButton.class).stream()
                .filter(botao -> texto.equals(botao.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Botão não encontrado: " + texto));
    }

    public static List<String> textosDosBotoes(Container raiz) {
        return todos(raiz, AbstractButton.class).stream()
                .map(AbstractButton::getText)
                .filter(texto -> texto != null && !texto.isBlank())
                .toList();
    }

    public static boolean existeRotulo(Container raiz, String texto) {
        return todos(raiz, JLabel.class).stream().anyMatch(rotulo -> texto.equals(rotulo.getText()));
    }

    private static <T extends Component> void coletar(Component atual, Class<T> tipo, List<T> encontrados) {
        if (tipo.isInstance(atual)) {
            encontrados.add(tipo.cast(atual));
        }
        if (atual instanceof Container container) {
            for (Component filho : container.getComponents()) {
                coletar(filho, tipo, encontrados);
            }
        }
    }
}
